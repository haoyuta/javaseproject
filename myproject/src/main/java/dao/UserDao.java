package dao;

import pojo.User;

import java.util.List;

/**
 * @className: UserDao
 * @description: TODO 类描述
 * @author: Mr.JiangXinYu
 * @date: 2023/08/14 17:39
 * @Company: Copyright© [日期] by [作者或个人]
 **/
public interface UserDao {
    //序列化方法
    public void serializeUser(List list);

    //注册方法
    void register(List<User> list);

    //登录方法
    User login(List<User> lists);

    //解锁方法
    boolean unLock(User user);

    //话费充值
    void phoneBill(List<User> userList);
}
