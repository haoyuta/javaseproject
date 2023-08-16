package dao.impl;
import dao.cons.Cons;
import exception.PasswordMismatchException;
import exception.UserNameNotFountException;
import pojo.User;
import dao.UserDao;
import org.apache.log4j.Logger;
import service.PackageService;
import service.impl.PackageServiceImpl;
import util.UserUtil;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Scanner;

/**
 * @className: UserDaoImpl
 * @description: 用户方法类
 **/
public class UserDaoImpl implements UserDao {
    //日志对象
    public static final Logger logger = Logger.getLogger(UserDaoImpl.class);
    //输入对象
    public static Scanner sc=new Scanner(System.in);
    //套餐服务对象
    private static PackageService packageService= new PackageServiceImpl();

    //序列化对象集合
    @Override
    public void serializeUser(List list)  {
        if(list.size()==0 || list==null) return;
        File file=new File(Cons.FILE_ADDTESS);
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try (ObjectOutputStream oos=new ObjectOutputStream(new FileOutputStream(file)))
        {
            oos.writeObject(list);

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * 用户注册功能
     * @param list
     */
    @Override
    public void register(List<User> list) {
        //先选择卡号
        String[] phoneNum = UserUtil.getPhoneNum();
        System.out.print("请选择卡号(输入1~9的序号)：");
        int phoneChoice = sc.nextInt();
        //选择的卡号
        String getPhoneNum=phoneNum[phoneChoice-1];

        //套餐选择
        System.out.print("1.话痨套餐   2.网虫套餐   3.超人套餐   请输入套餐(输入序号1~3)：");
        int packChoice = sc.nextInt();

        while (true) {
            System.out.print("请输入姓名：");
            String userName = sc.next();
            System.out.print("请输入密码：");
            String passWord = sc.next();
            //验证用户名和密码格式是否正确
            boolean b1 = UserUtil.checkingFormat(userName, passWord);
            if(!b1) continue;
            //验证用户信息是否存在
            boolean b2 = UserUtil.checkingUser(userName, list);
            if(!b2) continue;

            System.out.print("请输入预存花费金额(最低100)：");
            double money = sc.nextDouble();
            //根据选择的套餐扣费，并返回对象
            User newUser = packageService.totalPackage(packChoice, money);
            //当用户名和密码都正确且不存在时，将信息存入集合
            newUser.setUsername(userName);
            newUser.setPassword(passWord);
            newUser.setPhoneNum(getPhoneNum);
            newUser.setPackType(packChoice);
            list.add(newUser);

            //遍历集合，打印个人信息
            System.out.println("注册成功! 卡号："+getPhoneNum+" 用户名："+userName
            +" 当前余额："+newUser.getBalance());

            //打印套餐信息
            if(packChoice==1){
                System.out.println("话痨套餐:通话时长：50分钟，短信条数：30条/月。");
                //将当前用户消费信息输出到文件中
                String expenseMes="办理了话痨套餐，消费58元";
                try {
                    UserUtil.writeFile(Cons.FILE_EXPEND+userName+".txt",expenseMes);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(packChoice==2){
                System.out.println("网虫套餐:上网流量为3.0GB/月。");
                //将当前用户消费信息输出到文件中
                String expenseMes="办理了网虫套餐，消费68元";
                try {
                    UserUtil.writeFile(Cons.FILE_EXPEND+userName+".txt",expenseMes);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(packChoice==3){
                System.out.println("超人套餐:通话时长：20分钟，短信条数：50条，上网流量：1GB/月。");
                //将当前用户消费信息输出到文件中
                String expenseMes="办理了超人套餐，消费78元";
                try {
                    UserUtil.writeFile(Cons.FILE_EXPEND+userName+".txt",expenseMes);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return;
        }
    }


    /**
     * 登录功能
     * @param userList
     */
    @Override
    public User login(List<User> userList) {
        if (userList.size() == 0) {
            logger.error("无用户信息，请先注册！");
            return null;
        }
        System.out.print("请输入用户卡号：");
        String phoneNum = sc.next();
        System.out.print("请输入密码：");
        String passWord = sc.next();
        //遍历集合
        for (User user : userList) {
            //当找到了用户卡号再继续
            if (phoneNum.equals(user.getPhoneNum())) {
                //判断用户是否永久锁定
                if(user.getIsLock()==true){
                    System.out.println("用户已永久锁定！");
                    return null;
                }
                //解锁方法
                if (!unLock(user)) return null;

                //说明登录成功,并返回对象
                if (passWord.equals(user.getPassword())) {
                    System.out.println("欢迎您：" + user.getUsername());

                    //登录成功后显示是否有消息
                    UserUtil.findMesExist(user,userList);

                    user.setIsLogin(true);
                    //错误次数归0
                    user.setErrorNum(0);
                    return user;
                }

                if (user.getErrorNum() == 0) {
                    user.setLockTime(System.currentTimeMillis() + 60000);
                    user.setErrorNum(1);
                    throw new PasswordMismatchException("密码第一次输入错误! 锁定1分钟！");
                } else if (user.getErrorNum() == 1) {
                    user.setLockTime(System.currentTimeMillis() + 120000);
                    user.setErrorNum(2);
                    throw new PasswordMismatchException("密码第二次输入错误! 锁定2分钟！");
                } else {
                    user.setErrorNum(3);
                    user.setIsLock(true);
                    throw new PasswordMismatchException("密码第三次输入错误，用户永久锁定！");
                }
            }
        }
            //遍历结束，说明用户没找到
            throw new UserNameNotFountException("用户名不存在，登录失败！");
    }



    //解锁方法
    @Override
  public boolean unLock(User user){
     //获取当前系统时间
     Long nowTime=System.currentTimeMillis();
     //在锁定时间并且没有永久锁定
     if (user.getLockTime()>nowTime && user.getIsLock()==false) {
         int seconds= (int) ((user.getLockTime()-nowTime)/1000);
         if(seconds<1) System.out.println("用户即将解锁，请耐心等待。");
         System.out.println("该用户已经锁定,"+seconds+"秒后解锁！");
         return false;
     }
     //解锁成功
     return true;
 }


     //话费充值
    @Override
    public void phoneBill(List<User> userList){
        System.out.print("请输入要充值的用户卡号：");
        String phoneNum = sc.next();
        for (User user : userList) {
            //说明登录用户里存在
            if(phoneNum.equals(user.getPhoneNum()) && user.getIsLogin()==true){
                while (true) {
                    System.out.print("请输入要充值的金额(需为50的倍数)：");
                    double addMoney= sc.nextDouble();
                    //充值金额需为50的倍数并且充值后钱不能为0
                    if(addMoney%50==0 && (addMoney+user.getBalance()>0) && addMoney>0){
                        //设置余额
                        user.setBalance(addMoney+user.getBalance());
                        System.out.println(user.getUsername()+"的余额为："+user.getBalance());
                        return;
                    }
                    System.out.println("金额充值数不符合规定，请重新输入！");
                }
            }
        }
        //遍历完成，说明用户未登录
        System.out.println("该用户未登录，请先登录！");
    }
}
