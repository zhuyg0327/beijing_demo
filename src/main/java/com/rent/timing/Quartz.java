package com.rent.timing;


import com.rent.entity.SysJob;
import com.rent.util.quartz.ScheduleUtils;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Component
public class Quartz {

    @Autowired
    private Scheduler scheduler;

    @PostConstruct
    public void init() throws Exception {
        SysJob sysJob = new SysJob();
        sysJob.setJobId(001L);
        sysJob.setJobName("测试quartz定时任务");
        sysJob.setJobGroup("default");
        sysJob.setInvokeTarget("task.task1()");
        sysJob.setCronExpression("*/5 * * * * ?");
        sysJob.setMisfirePolicy("3");
        sysJob.setConcurrent("1");
        sysJob.setStatus("0");

        List<SysJob> jobList=new ArrayList<>();
        jobList.add(sysJob);
        scheduler.clear();
        for (SysJob job : jobList) {
            System.out.println("执行");
            ScheduleUtils.createScheduleJob(scheduler, job);
        }
    }
}
