package dao;

import pojo.User;

import java.util.List;

/**
 * @className: DouDou
 * @description:  兜兜5G用户菜单功能接口
 **/
public interface DouDou {

    //打印套餐余量
    public void packageAllowance(User user);

    //办理退网
    void outNetWork(List<User>userList,User loginUser);

    //打印账单方法
    void printBill(User loginUser);
}
