package service;

import pojo.User;

/**
 * @className: Package
 * @description: 套餐服务接口
 **/
public interface PackageService {
    //话痨套餐服务
    User talkPackage(Double money);

    //网虫套餐服务
    User onlinePackage(Double money);

    //超人套餐服务
    User superPackage(Double money);

    //总体套餐扣费服务
    User totalPackage(int choice,Double money);
}
