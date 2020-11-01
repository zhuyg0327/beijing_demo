package com.rent.dao;

import com.rent.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserDao {
    //查询用户信息根据id
    User queryById(Map<String, Object> map);

    //批量插入
    void insertBatch(@Param("list") List<User> list);

    //查询所有用户
    List<User> queryall();

    void deleteAll();

    //批量更新
    void updateBatch(@Param("list") List<User> list);
}
