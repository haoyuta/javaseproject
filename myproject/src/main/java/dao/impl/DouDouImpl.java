package dao.impl;

import dao.DouDou;
import dao.cons.Cons;
import pojo.User;
import util.UserUtil;
import java.util.List;

/**
 * @className: DouDouImpl
 * @description: 兜兜5G用户菜单功能实现类
 **/
public class DouDouImpl implements DouDou {


    //打印套餐余量
    @Override
    public void packageAllowance(User user){
        System.out.println(user.getUsername()+"的套餐余量如下：");
        System.out.println("通话时长："+user.getTalkTime());
        System.out.println("短信条数："+user.getMesNum());
        System.out.println("上网流量："+user.getOnlineTraffic());
    }


    //办理退网方法
    @Override
    public void outNetWork(List<User> userList,User loginUser) {
        for (User user : userList) {
            if(user.getUsername().equals(loginUser.getUsername())){
                //删除用户消费账单
                UserUtil.deleteFile(Cons.FILE_EXPEND+user.getUsername()+".txt");
                userList.remove(user);
                System.out.println("退网成功，感谢您的使用！");
                return;
            }
        }
    }

    //打印账单
    @Override
    public void printBill(User loginUser) {
        if(loginUser.getBalance()>=0)
        System.out.println("剩余金额："+loginUser.getBalance());
        else
            System.out.println("欠费："+(-loginUser.getBalance()));

        System.out.println("总共消费："+loginUser.getTotalExp());
    }
}
