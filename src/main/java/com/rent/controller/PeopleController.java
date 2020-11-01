package com.rent.controller;

import com.rent.entity.Arrange;
import com.rent.entity.Leader;
import com.rent.entity.People;
import com.rent.service.LeaderService;
import com.rent.service.PeopleService;
import com.rent.util.DateUtil;
import com.rent.util.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@RestController
@CrossOrigin(allowCredentials = "true")
@RequestMapping("/api/people")
public class PeopleController {
    @Autowired
    PeopleService peopleService;
    @Autowired
    LeaderService leaderService;
    private static final Logger logger = LoggerFactory.getLogger(PeopleController.class);

    //查询所有
    @RequestMapping("/look")
    public Result query(String startTime) throws ParseException {
        Result result = null;
        List<People> list = peopleService.queryAll();
        List<Leader> list1 = leaderService.queryAll();
        List<Arrange> arranges = new ArrayList<>();
        Date day = DateUtil.parase(startTime, "yyyy-mm-dd");
        int i = 1;
        int k = 1;
        if (!list.isEmpty() && !list1.isEmpty()) {
            for (People people : list) {
                if (i <= list.size()) {
                    int a = (int) Math.floor(i / 7);
                    int b = i % 7;
                    Date days = DateUtil.getDateAfter(day, i);
                    Arrange arrange = new Arrange();
                    arrange.setDutyDate(DateUtil.format(days, "yyyy-mm-dd"));
                    arrange.setUserId(people.getUserId());
                    arrange.setUserName(people.getUserName());
                    //当值班领导够用的时候，即普通人数量小于7倍的领导数量，领导有多余余人员
                    if (a < list1.size()) {
                        if (a < 1) {//即第一批值班人员
                            arrange.setLeaderId(list1.get(0).getLeaderId());
                            arrange.setLeaderName(list1.get(0).getLeaderName());
                        } else if (b == 0) {//此时值班人员刚好是7的倍数
                            arrange.setLeaderId(list1.get(a - 1).getLeaderId());
                            arrange.setLeaderName(list1.get(a - 1).getLeaderName());
                        } else {//第二批及其之后的值班人员
                            arrange.setLeaderId(list1.get(a).getLeaderId());
                            arrange.setLeaderName(list1.get(a).getLeaderName());
                        }
                    } else if (a == list1.size() && b == 0) {//领导刚好够用，即领导和人员刚好满足1:7关系
                        arrange.setLeaderId(list1.get(a - 1).getLeaderId());
                        arrange.setLeaderName(list1.get(a - 1).getLeaderName());
                    } else if (a == list1.size() && b != 0) {
                        arrange.setLeaderId(list1.get(0).getLeaderId());
                        arrange.setLeaderName(list1.get(0).getLeaderName());
                    } else {
                        arrange.setLeaderId(list1.get(k - 1).getLeaderId());
                        arrange.setLeaderName(list1.get(k - 1).getLeaderName());
                        if (b == 0) {
                            k++;
                        }
                        if (k > list1.size()) {
                            k = 1;
                        }
                    }
                    arrange.setWeek(DateUtil.getDayWeekOfDate1(days));
                    arrange.setUpdateTime(DateUtil.format(new Date(), "yyyy-mm-dd"));
                    arranges.add(arrange);
                }
                i++;
            }
        }
        if (arranges != null) {
            result = new Result("200", "查询成功", arranges);
        } else {
            result = new Result("500", "查询失败", null);
        }
        return result;
    }

    //查询所有
    @RequestMapping("/looks")
    public Result querys(String startTime) throws ParseException {
        Result result = null;
        List<People> list = peopleService.queryAll();
        List<Leader> list1 = leaderService.queryAll();
        List<Arrange> arranges = new ArrayList<>();
        Date day = DateUtil.parase(startTime, "yyyy-mm-dd");
        int i = 1;
        int k = 1;
        if (!list.isEmpty() && !list1.isEmpty()) {
            for (People people : list) {
                if (i <= list.size()) {
                    int a = (int) Math.floor(i / 7);
                    int b = i % 7;
                    Date days = DateUtil.getDateAfter(day, i);
                    Arrange arrange = new Arrange();
                    arrange.setDutyDate(DateUtil.format(days, "yyyy-mm-dd"));
                    arrange.setUserId(people.getUserId());
                    arrange.setUserName(people.getUserName());
                    arrange.setLeaderId(list1.get(k - 1).getLeaderId());
                    arrange.setLeaderName(list1.get(k - 1).getLeaderName());
                    if (b == 0) {
                        k++;
                    }
                    if (k > list1.size()) {//当领导不够用的时候，从第一个领导开始重新排
                        k = 1;
                    }
                    arrange.setWeek(DateUtil.getDayWeekOfDate1(days));
                    arrange.setUpdateTime(DateUtil.format(new Date(), "yyyy-mm-dd"));
                    arranges.add(arrange);
                }
                i++;
            }
        }
        if (arranges != null) {
            result = new Result("200", "查询成功", arranges);
        } else {
            result = new Result("500", "查询失败", null);
        }
        //ResponseUtil.ofMessage("200",arranges);
        return result;
    }
}
