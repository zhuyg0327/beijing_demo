package com.rent.dao;

import com.rent.entity.Leader;
import org.apache.ibatis.annotations.Mapper;


import java.util.List;

@Mapper
public interface LeaderDao {
    //查询所有用户
    List<Leader> queryall();
}
