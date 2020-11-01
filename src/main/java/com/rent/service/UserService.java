package com.rent.service;

import com.rent.entity.User;

import java.util.List;
import java.util.Map;

public interface UserService {
    //查询用户信息根据id
    User queryById(Map<String, Object> map);

    //批量插入
    void insertBatch(List<User> list);
    //批量更新
    void updateBatch(List<User> list);
    //查询所有
    public List<User> queryAll();

    void deleteAll();
}
