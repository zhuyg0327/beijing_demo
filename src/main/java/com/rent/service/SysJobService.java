package com.rent.service;

import com.rent.entity.SysJob;

import java.util.List;

public interface SysJobService {
    /**
     * 查询所有调度任务
     *
     * @return 调度任务列表
     */
    List<SysJob> selectJobAll();
}
