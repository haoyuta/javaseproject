package service.impl;

import pojo.User;
import service.PackageService;

import java.util.ArrayList;

/**
 * @className: PackageServiceImpl
 * @description: 套餐服务实现
 **/
public class PackageServiceImpl implements PackageService {
    @Override
    public User talkPackage(Double money) {
        User user=new User(null,null,null,0,money-58,58D,false,false,0L,0,new ArrayList<>(10),new ArrayList<>(10),50,0,30);
        return user;
    }

    @Override
    public User onlinePackage(Double money) {
        User user=new User(null,null,null,0,money-68,68D,false,false,0L,0,new ArrayList<>(10),new ArrayList<>(10),0,3,0);
        return user;
    }

    @Override
    public User superPackage(Double money) {
        User user=new User(null,null,null,0,money-78,78D,false,false,0L,0,new ArrayList<>(10),new ArrayList<>(10),20,1,50);
        return user;
    }

    @Override
    public User totalPackage(int choice, Double money) {
        if(choice==1)   return talkPackage(money);
        else if(choice==2)  return onlinePackage(money);
        else if(choice==3)  return superPackage(money);
        else return null;
    }
}
