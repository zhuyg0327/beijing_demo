package com.rent.service.impl;

import com.rent.dao.UserDao;
import com.rent.entity.Leader;
import com.rent.entity.User;
import com.rent.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserDao userDao;

    @Override
    public User queryById(Map<String, Object> map) {
        return userDao.queryById(map);
    }

    @Override
    public void insertBatch(List<User> list) {
        userDao.insertBatch(list);
    }

    //查询所有
    @Override
    public List<User> queryAll() {
        List<User> user = userDao.queryall();
        return user;
    }

    @Override
    public void deleteAll() {
        userDao.deleteAll();
    }

    @Override
    public void updateBatch(List<User> list) {
        userDao.updateBatch(list);
    }
}
