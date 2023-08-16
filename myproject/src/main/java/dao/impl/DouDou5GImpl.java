package dao.impl;

import dao.DouDou5G;
import dao.cons.Cons;
import pojo.User;
import util.UserUtil;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

/**
 * @className: DouDou5GImpl
 * @description: 兜兜5G实现类
 **/
public class DouDou5GImpl implements DouDou5G {

    //设置一个强制终止打电话的变量
    private static boolean stopFlag = false;

    //输入对象
    public static Scanner sc=new Scanner(System.in);

    //对方的电话号码
    private static String otherPhone;

    //打电话方法
    public void call(User loginUser,List<User> userList){
        //判断输入的电话是否存在
        judge(loginUser,userList,1);

        //创建一个通话线程
        new Thread(() -> {
            //确认套餐名称
            String packName;
            if (loginUser.getPackType() == 1)   packName="话痨套餐";
            else if(loginUser.getPackType() == 1)   packName="网虫套餐";
            else packName="超人套餐";
            //确认通话时长
            Integer talkTime=loginUser.getTalkTime();
            //确认输出语句
            String lastWord1="开始计时。";
            String lastWord2="当前计费0.2元/分钟...";


            if(talkTime>0) {
                System.out.println("您当前的套餐为" + packName + ",通话剩余时长为" + talkTime + "分钟,"+lastWord1);
                //总通话时长
                Integer callingMin = calling();
                System.out.println("通话结束，当前与"+otherPhone+"的通话时长为:"+callingMin+"分钟");

                //通话时间大于通话剩余时长
                if(callingMin>talkTime){
                    //消费费用
                    double callPay= new BigDecimal((callingMin-talkTime)*0.2).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();


                    //扣除相应费用,并赋值
                    loginUser.setBalance(loginUser.getBalance()-callPay);
                    loginUser.setTalkTime(0);
                    System.out.println("本次共消费:"+callPay+"元！");

                    //记录消费
                    loginUser.setTotalExp(loginUser.getTotalExp()+callPay);


                    //将消费信息写入消费账单
                    try {
                        UserUtil.writeFile(Cons.FILE_EXPEND+loginUser.getUsername()+".txt","与"+otherPhone+"通话共消费:"+callPay+"元！");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else {
                    //剩余的通话时长
                    Integer reTime=talkTime-callingMin;
                    //直接扣除通话时长
                    loginUser.setTalkTime(reTime);
                    System.out.println("话费剩余:"+reTime+"分钟！");

                    //将消费信息写入消费账单
                    try {
                        UserUtil.writeFile(Cons.FILE_EXPEND+loginUser.getUsername()+".txt","与"+otherPhone+"通话共消耗话费:"+callingMin+"分钟！");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }else {
                System.out.println("您当前的套餐为" + packName + ",通话剩余时长为" + talkTime + "分钟,"+lastWord2);
                //总通话时长
                Integer callingMin = calling();
                System.out.println("通话结束，当前与"+otherPhone+"的通话时长为:"+callingMin+"分钟");
                //消费费用
                double callPay= new BigDecimal(callingMin*0.2).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();;

                //直接扣除费用,并赋值
                loginUser.setBalance(loginUser.getBalance()-callPay);
                System.out.println("本次共消费:"+callPay+"元！");

                //记录消费
                loginUser.setTotalExp(loginUser.getTotalExp()+callPay);

                //将消费信息写入消费账单
                try {
                    UserUtil.writeFile(Cons.FILE_EXPEND+loginUser.getUsername()+".txt","与"+otherPhone+"通话共消费:"+callPay+"元！");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //通话方法
    public  Integer calling(){
        stopFlag=false;
        System.out.println("正在呼叫...");
        int i=0;
        while(!stopFlag) {
            System.out.println("通话中，通话时长为:"+i+"分钟...");
            try {
                Thread.sleep(1000);
                if(!stopFlag) i+=1;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }        }
        return i;
    }

    //结束通话方法
    public  void endCalling(){
        stopFlag=true;
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送短信方法
     * @param loginUser
     * @param userList
     */
    @Override
    public void sendMes(User loginUser, List<User> userList) {
        //判断输入的电话是否存在
        User judgeUser = judge(loginUser, userList, 2);
        if(judgeUser.getIsLock()){
            System.out.println("对方账户永久封禁，无法对其发送消息！");
            return;
        }

        //先获取登录用户接收方电话集合
            List<String> otherPhoneNum = loginUser.getOtherPhoneNum();
            //登录用户设置接收方电话
            otherPhoneNum.add(otherPhone);
            loginUser.setOtherPhoneNum(otherPhoneNum);

            //先获取登录用户接收方信息集合
            List<String> message = loginUser.getMessage();

            //设置发送给接收方的信息
           BufferedReader reader=new BufferedReader(new InputStreamReader(System.in));
                System.out.print("请输入要发送给"+otherPhone+"号主的信息:");
            try {
                message.add(reader.readLine());
            } catch (IOException e) {
                e.printStackTrace();
            }
            loginUser.setMessage(message);

            //消息发送完之后开始计费
            //获取用户的短信条数
            Integer mesNum = loginUser.getMesNum();
            if(mesNum>0){
                //直接扣除短信数即可
                loginUser.setMesNum(mesNum-1);
            }else {
                //扣除费用
                loginUser.setBalance(new BigDecimal(loginUser.getBalance()-0.1).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue());

                //记录消费
                loginUser.setTotalExp(new BigDecimal(loginUser.getBalance()+0.1).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue());


                //添加消费消息
                String expenseMes="向"+otherPhone+"号机主发送了一条短信，消费0.1元";
                try {
                    UserUtil.writeFile(Cons.FILE_EXPEND+loginUser.getUsername()+".txt",expenseMes);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
    }

    /**
     * 接收短信方法
     * @param userList
     */
    @Override
    public void getMes(User loginUser,List<User> userList) {
        //接收信息数
        int num=0;
        //遍历用户集合
        for (User user : userList) {
            //获取用户接收方电话集合
            List<String> otherPhoneNum = user.getOtherPhoneNum();
            //获取登录用户接收方信息集合
            List<String> message = user.getMessage();

            //遍历集合
            for (int i = 0; i < otherPhoneNum.size(); i++) {
                //说明有用户传递信息给自己
                if(loginUser.getPhoneNum().equals(otherPhoneNum.get(i))){
                    String mes=user.getPhoneNum()+"号主传递给自己的信息："+message.get(i);
                    //下标就是对应自己的信息
                    System.out.println(mes);


                    //定义格式化形式
                    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss EE a");
                    String writeMes=mes+"        "+sdf.format(System.currentTimeMillis());
                    //将消息写入历史消息文件
                    try {
                        UserUtil.writeFile(Cons.FILE_MesHistory+loginUser.getUsername()+".txt",writeMes);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    //当接受完信息后删除用户信息
                    otherPhoneNum.remove(i);
                    message.remove(i);
                    num++;

                    i--; //因为移除一个元素，该元素后面的都会往前移一位，需要将i-1
                }
            }
        }
        if(num==0) System.out.println("暂无信息。");
    }

    //判断电话号码
    public User judge(User loginUser,List<User> userList,int i){
        while (true) {
            if(i==1) System.out.print("请输入要呼叫的电话号码：");
            else System.out.print("请输入要发送消息的电话号码：");
            otherPhone = sc.next();
            User user = UserUtil.findUser(otherPhone, userList);
            if(user==null)  System.out.println("用户不存在，请重新输入！");
            else if (user.getPhoneNum()==loginUser.getPhoneNum()) System.out.println("这是您的电话，请重新输入！");
            else return user;
        }
    }
}