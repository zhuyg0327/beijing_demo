package com.rent.timing;


import com.rent.entity.SysJob;
import com.rent.service.SysJobService;
import com.rent.util.quartz.ScheduleUtils;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class Quartz {

    @Autowired
    private Scheduler scheduler;
    @Autowired
    private SysJobService sysJobService;

    /**
     * 初始化构造函数，必须执行
     *
     * @throws Exception
     */
    @PostConstruct
    public void init() throws Exception {

//        查询任务调度表中的数据
        List<SysJob> jobList = sysJobService.selectJobAll();
//        每次初始化先清理定时任务信息，然后重新创建
        scheduler.clear();
        for (SysJob job : jobList) {
            System.out.println("执行");
            ScheduleUtils.createScheduleJob(scheduler, job);
        }
    }
}
