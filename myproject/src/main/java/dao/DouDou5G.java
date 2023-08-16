package dao;
import pojo.User;
import java.util.List;

/**
 * @className: DouDou5G
 * @description: 兜兜5G功能接口
 **/
public interface DouDou5G {

    //打电话方法
    void call(User loginUser,List<User> userList);

     //结束通话方法
    void endCalling();

    //发短信方法
    void sendMes(User loginUser,List<User> userList);

    //接收短信方法
    void getMes(User loginUser,List<User> userList);

}
