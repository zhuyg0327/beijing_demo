package com.rent.timing;

import org.springframework.stereotype.Component;

@Component("task")
public class Task {
    public void task1(){
        System.out.println("调度任务quartz");
    }
}
