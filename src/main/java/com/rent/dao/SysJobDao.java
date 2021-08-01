package com.rent.dao;

import com.rent.entity.SysJob;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SysJobDao {
    /**
     * 查询所有调度任务
     *
     * @return 调度任务列表
     */
    List<SysJob> selectJobAll();
}
