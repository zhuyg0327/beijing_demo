package com.rent.dao;

import com.rent.entity.People;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface PeopleDao {
     //查询所有用户
     List<People> queryall();
}
