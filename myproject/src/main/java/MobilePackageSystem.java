import dao.DouDou;
import dao.DouDou5G;
import dao.cons.Cons;
import dao.impl.DouDou5GImpl;
import dao.impl.DouDouImpl;
import exception.PasswordMismatchException;
import exception.UserNameNotFountException;
import pojo.User;
import dao.UserDao;
import dao.impl.UserDaoImpl;
import util.UserUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import static util.Menu.*;

/**
 * @className: MyProject
 * @description: 移动套餐管理系统
 **/
public class MobilePackageSystem {
    //存储用户
    public static List<User> users=new ArrayList<>();

    //用户方法类对象
    private static UserDao userDao=new UserDaoImpl();

    //兜兜5G移动用户对象
    private static DouDou douDou=new DouDouImpl();

    //兜兜5G功能用户对象
    private static DouDou5G douDou5G=new DouDou5GImpl();

    //当前登录用户
    public static User loginUser;

    //在类加载时反序列化用户对象
    static {
        File file=new File(Cons.FILE_ADDTESS);
        if(file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                try {
                    users= (List<User>) ois.readObject();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {

        for (User user : users) {
            System.out.println(user.toString());
        }
        while (true) {
            //系统主页面
            mainMenu();
            Scanner sc=new Scanner(System.in);
            int choice=sc.nextInt();

            switch (choice){
                case 1:
                    //调用登录方法
                    try {
                         loginUser = userDao.login(users);
                        if(loginUser!=null){
                            doudou:while (true) {
                                //登录之后显示兜兜移动用户菜单
                                douDouMenu();
                                int douChoice = sc.nextInt();
                                switch (douChoice){
                                    case 1:
                                        //调用本月账单查询方法
                                        douDou.printBill(loginUser);
                                        break;
                                    case 2:
                                        //调用套餐余量查询
                                        douDou.packageAllowance(loginUser);
                                        break;
                                    case 3:
                                        //打印消费菜单
                                        UserUtil.showFile(Cons.FILE_EXPEND+loginUser.getUsername()+".txt");
                                        break;
                                    case 4:
                                        //办理退网
                                        douDou.outNetWork(users,loginUser);
                                        break doudou;
                                    default:
                                        //直接返回上一级
                                       break doudou;
                                }
                            }

                        }
                    } catch (UserNameNotFountException e) {
                        System.out.println(e.getMessage());
                    }catch (PasswordMismatchException e){
                        System.out.println(e.getMessage());
                    }
                    break;

                case 2:
                    //调用注册方法
                    userDao.register(users);
                    break;

                case 3:
                    doudou5G: while (true) {
                        //使用兜兜5G菜单
                        boolean result = douDouMenu2(loginUser);
                        if(!result) break doudou5G;
                        int douChoice5G = sc.nextInt();
                        switch (douChoice5G) {
                            case 1:
                                //调用打电话方法
                                douDou5G.call(loginUser,users);
                                break;
                            case 2:
                                //调用发短信方法
                                douDou5G.sendMes(loginUser,users);
                                break;
                            case 3:
                                //调用终止通话方法
                                douDou5G.endCalling();
                                break;
                            case 4:
                                //调用接收短信方法
                                douDou5G.getMes(loginUser,users);
                                break;
                            case 5:
                                //调用查看历史消息方法
                                UserUtil.showFile(Cons.FILE_MesHistory+loginUser.getUsername()+".txt");
                                break;
                            default:
                                //直接返回上一级
                                break doudou5G;
                        }
                    }
                    break;

                case 4:
                    //调用话费充值方法
                    userDao.phoneBill(users);
                    break;

                case 5:
                    //调用显示资费说明方法
                    UserUtil.showFile(Cons.FILE_EXPENSES);
                    break;

                case 6:
                    //序列化对象
                    userDao.serializeUser(users);
                    System.out.println("感谢使用兜兜5G系统，再见！");
                    //将所有用户登录状态设置为false
                    for (User user : users) {
                        user.setIsLogin(false);
                    }
                    System.exit(0);

                default:
                    System.out.println("新功能正在开发中，敬请期待！");
            }
        }
    }
}
