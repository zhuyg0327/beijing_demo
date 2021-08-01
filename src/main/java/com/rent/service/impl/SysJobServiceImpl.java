package com.rent.service.impl;

import com.rent.dao.SysJobDao;
import com.rent.entity.SysJob;
import com.rent.service.SysJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName SysJobServiceImpl
 * @Description TODO
 * @Author zyg
 * @Date 2021/8/1 17:04
 * @Version 3.0
 **/
@Service
public class SysJobServiceImpl implements SysJobService {

    @Autowired
    private SysJobDao sysJobDao;

    /**
     * 查询所有调度任务
     *
     * @return 调度任务列表
     */
    @Override
    public List<SysJob> selectJobAll() {
        return sysJobDao.selectJobAll();
    }
}
