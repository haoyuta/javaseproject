package util;
import pojo.User;
import java.io.*;
import java.util.*;

/**
 * @className: UserUtil
 * @description: 用户工具类

 **/
public final class UserUtil {
    //输入对象
    public static Scanner sc=new Scanner(System.in);

    //验证用户格式
    public static boolean checkingFormat(String userName, String passWord) {
        //判断用户名格式是否正确
        if (userName.matches("^[a-zA-Z][a-zA-Z0-9]{4,7}$") == false) {
            System.out.println("用户名输入格式不正确！请重新输入");
            return false;
        }
        //判断密码格式是否正确
        if (passWord.matches("^(?=.*[a-zA-Z])(?=.*\\d)(?=.*_).{6,10}$") == false) {
            System.out.println("密码输入格式不正确！请重新输入");
            return false;
        }
        return true;
    }


    //验证用户是否存在
    public static boolean checkingUser(String userName, List<User> list){
        //遍历集合，看是否存在相同用户信息
        for (User user : list) {
            if (userName.equals(user.getUsername())){
                System.out.println("该用户名已存在，请换一个！");
                return false;
            }
        }
        return true;
    }


    //随机生成用户卡号,返回一个卡号数组
    public static String[] getPhoneNum(){
        //电话号码前三位
        String[] numStart={"134","135","136","137","138","139","150","151","152","157","159","187","188"};
        Set<String> phoneNumSet=new HashSet<>();
        while (true) {
            //随机获取前三位
            Random random = new Random();
            String start = numStart[random.nextInt(numStart.length)];

            //随机获取后八位
            Long l = (long)(Math.random()*100000000);
            String end = l.toString();

            //拼接电话号码
            String phoneNum=start+end;
            if(phoneNum.length()==11)
            phoneNumSet.add(phoneNum);
            if(phoneNumSet.size()==9)
                break;
        }
        //新建一个字符串数组
        String[] phoneNums= phoneNumSet.toArray(new String[8]);
        //打印用户卡号
        System.out.println("******可选择卡号*****");
        for (int i = 0; i < phoneNums.length; i++) {
            System.out.print(i+1 + "." + phoneNums[i] + "\t");
            if(i==2 || i==5 || i==8) System.out.println();
        }
        return phoneNums;
    }


    //输入文件方法
    public static void showFile(String fileAddress){
        //采用输入流读取文件信息
        File file=new File(fileAddress);
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try (FileReader fileReader=new FileReader(file)) {
            char[] chars=new char[100];
            int len;
            while ((len=fileReader.read(chars))!=-1){
                //解码输出
                System.out.println(new String(chars,0,len));
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }


    //写入文件方法
    public static void writeFile(String fileAddress,String mes) throws IOException {
        //采用输出流输出文件信息
        File file=new File(fileAddress);
        if(!file.exists()) file.createNewFile();
        try (FileWriter fileWriter=new FileWriter(file,true)) {
            fileWriter.write(mes);
            fileWriter.write("\r\n");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //删除文件方法
    public static void deleteFile(String fileAddress){
        File file=new File(fileAddress);
        if(file.exists()) file.delete();
    }

    //通过电话查找用户
    public static User findUser(String phoneNum,List<User> userList){
            for (User user : userList) {
                if(phoneNum.equals(user.getPhoneNum())){
                    return user;
                }
            }
            return null;
        }


    //查找登录用户有没有信息需要查收
    public static void findMesExist(User loginUser,List<User> userList){
        for (User user : userList) {
            //获取用户接收方电话集合
            List<String> otherPhoneNum = user.getOtherPhoneNum();
            //遍历集合
            for (int i = 0; i < otherPhoneNum.size(); i++) {
                //说明有用户传递信息给自己
                if (loginUser.getPhoneNum().equals(otherPhoneNum.get(i))) {
                    //说明有自己的信息
                    System.err.println("有机主发信息给您，记得查收");
                    return;
                }
            }
        }

    }
}

