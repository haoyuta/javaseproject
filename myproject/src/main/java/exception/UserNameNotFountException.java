package exception;

/**
 * @className: UserNameNotFountException
 * @description: TODO 类描述
 * @author: Mr.JiangXinYu
 * @date: 2023/07/31 19:30
 * @Company: Copyright© [日期] by [作者或个人]
 **/
public class UserNameNotFountException extends RuntimeException{

    public UserNameNotFountException(String message) {
        super(message);
    }
}
