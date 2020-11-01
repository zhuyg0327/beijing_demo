package com.rent.service.impl;

import com.rent.dao.BaseAppUserDao;
import com.rent.entity.BaseAppUser;
import com.rent.entity.People;
import com.rent.service.BaseAppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class BaseAppUserServiceImpl implements BaseAppUserService {
    @Autowired
    BaseAppUserDao dao;

    //登录
    @Override
    public BaseAppUser login(Map<String, Object> map) {
        BaseAppUser user = dao.login(map);
        return user;
    }

    //查询用户信息根据id
    @Override
    public BaseAppUser queryById(Map<String, Object> map) {
        BaseAppUser user = dao.queryById(map);
        return user;
    }

    public List<BaseAppUser> findByOrganid(String organId) {
        List<BaseAppUser> list = dao.findByOrganid(organId);
        return list;
    }
    //查询所有
    @Override
    public List<BaseAppUser> queryAll() {
        List<BaseAppUser> user = dao.queryall();
        return user;
    }
}
