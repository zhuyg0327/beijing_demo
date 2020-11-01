package com.rent.service.impl;

import com.rent.dao.PeopleDao;
import com.rent.entity.People;
import com.rent.service.PeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class PeopleServiceImpl implements PeopleService {
    @Autowired
    PeopleDao dao;

    //查询所有
    @Override
    public List<People> queryAll() {
        List<People> people = dao.queryall();
        return people;
    }
}
