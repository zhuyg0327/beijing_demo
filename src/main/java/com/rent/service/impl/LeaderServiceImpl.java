package com.rent.service.impl;

import com.rent.dao.LeaderDao;
import com.rent.entity.Leader;
import com.rent.service.LeaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LeaderServiceImpl implements LeaderService {
    @Autowired
    LeaderDao dao;

    //查询所有
    @Override
    public List<Leader> queryAll() {
        List<Leader> leader = dao.queryall();
        return leader;
    }
}
