package pojo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.List;

/**
 * @className: User
 * @description: 用户类
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {
    private String username;
    private String password;
    //电话卡号
    private String phoneNum;
    //套餐类型
    private Integer packType;
    //剩余话费
    private Double balance;
    //总共消费
    private Double totalExp;


    //是否永久锁定
    private Boolean isLock;
    //是否登录
    private Boolean isLogin;
    //锁定时间戳
    private Long lockTime;
    //错误次数
    private Integer errorNum;


    //短信接收方电话号码,可以向多个用户发送信息
    private List<String> otherPhoneNum;
    //短信内容,多个用户的信息
    private List<String> message;


    //通话时长，单位分钟
    private Integer talkTime;
    //上网流量，单位GB
    private Integer onlineTraffic;
    //短信条数
    private Integer mesNum;
}