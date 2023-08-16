package util;

import pojo.User;

/**
 * @className: Menu
 * @description: 页面类
 **/
public final class Menu {

    //系统主界面
    public static void mainMenu(){
        System.out.println("*******************欢迎使用兜兜5G移动业务大厅*******************");
        System.out.println("1.用户登录   2.用户注册   3.使用兜兜5G   4.话费充值   5.资费说明   6.退出系统");
        System.out.print("请选择:");
    }

    //兜兜用户菜单
    public static void douDouMenu(){
        System.out.println("*****兜兜5G移动用户菜单*****");
        System.out.println("1.  本月账单查询");
        System.out.println("2.  套餐余量查询");
        System.out.println("3.  打印消费详单");
        System.out.println("4.  办理退网");
        System.out.print("功能选择(输入1~5选择,其他键返回上一级)：");
    }

    //兜兜菜单
    public static boolean douDouMenu2(User loginUser){
        if(loginUser==null){
            System.out.println("当前无用户登录，请先登录！");
            return false;
        }
        System.out.println("******使用兜兜5G******");
        System.out.println("1.  打电话");
        System.out.println("2.  发短信");
        System.out.println("3.  终止通话");
        System.out.println("4.  接收新短信");
        System.out.println("5.  查看历史消息");
        return true;
    }
}
