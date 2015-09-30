package com.taolijie.schedule.job;

import com.taolijie.schedule.service.ScheduleService;
import org.quartz.*;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Created by whf on 9/30/15.
 */
@Component
public class RunOnceJob implements Job, ApplicationContextAware {
    private static ApplicationContext ctx;

    private static ScheduleService scheduleService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println("hello");

        // 执行完成后马上删除任务本身
        try {
            delJob(context.getJobDetail());
        } catch (SchedulerException e) {
            throw new JobExecutionException("delete job failed");
        }
    }

    private void delJob(JobDetail jd) throws SchedulerException {
        String name = jd.getKey().getName();

        // 如果组件不存在
        // 则从spring取组件
        if (null == scheduleService) {
            scheduleService = (ScheduleService) ctx.getBean("defaultScheduleService");
        }

        scheduleService.delJob(name);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ctx = applicationContext;
    }
}
