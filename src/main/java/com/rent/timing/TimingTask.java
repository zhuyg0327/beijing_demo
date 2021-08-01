package com.rent.timing;

import com.rent.util.DateUtil;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 定时任务
 */
@Component
public class TimingTask {
    //自动同步时间
    private static Long starttime;
    //java 定时器
    private Timer timer = null;
    //定时器任务
    private static TimerTask timerTask = null;
    //定时器状态：true：定时器开启；false：定时器关闭
    private static boolean status = true;

    /**
     * 启动程序时默认启动定时任务
     * 启动项目1分钟后开始执行(delay：60000毫秒)，之后每两分钟执行一次
     */
    public TimingTask() {
        if (timer == null) {
            timer = new Timer();
        }
        timer.scheduleAtFixedRate(getInstance(), 60000, 120000);
    }

    /**
     * 获取定时任务
     *
     * @return
     */
    public TimerTask getInstance() {
        if (timerTask == null || !status) {
            status = true;
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    System.out.println("开启定时任务");
                    System.out.println(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
                }
            };
        }
        return timerTask;
    }
}
