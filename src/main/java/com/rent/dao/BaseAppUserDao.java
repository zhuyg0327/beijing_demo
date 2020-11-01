package com.rent.dao;

import com.rent.entity.BaseAppUser;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BaseAppUserDao {
    //登录
    BaseAppUser login(Map<String, Object> map);

    //查询用户信息根据id
    BaseAppUser queryById(Map<String, Object> map);

    List<BaseAppUser> findByOrganid(String organId);

    //查询所有用户
    List<BaseAppUser> queryall();

}
