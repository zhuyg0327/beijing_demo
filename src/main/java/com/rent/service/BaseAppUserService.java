package com.rent.service;

import com.rent.entity.BaseAppUser;

import java.util.List;
import java.util.Map;

public interface BaseAppUserService {
    //登录
    BaseAppUser login(Map<String, Object> map);

    //查询用户信息根据id
    BaseAppUser queryById(Map<String, Object> map);

    List<BaseAppUser> findByOrganid(String organId);

    //查询所有
    public List<BaseAppUser> queryAll();
}
