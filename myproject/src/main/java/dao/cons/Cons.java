package dao.cons;
import java.io.File;

/**
 * @className: Cons
 * @description: TODO 类描述
 * @author: Mr.JiangXinYu
 * @date: 2023/08/10 19:48
 * @Company: Copyright© [日期] by [作者或个人]
 **/
public interface Cons {

     //序列化文件地址
     String FILE_ADDTESS="D:"+ File.separator+"ProjectFile"+File.separator+"users.txt";

     //资费说明文件地址
     String FILE_EXPENSES="D:"+ File.separator+"ProjectFile"+File.separator+"Expenses.txt";

     //消费详单地址,需拼接用户名称
     String FILE_EXPEND="D:"+ File.separator+"ProjectFile"+File.separator+"userExpense"+File.separator;

     //历史消息地址,需拼接用户名称
     String FILE_MesHistory="D:"+ File.separator+"ProjectFile"+File.separator+"mesHistory"+File.separator;

}
