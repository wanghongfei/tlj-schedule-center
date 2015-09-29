package com.taolijie.schedule;

import com.taolijie.schedule.job.TestJob;
import org.quartz.*;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

/**
 * 程序启动类
 * Created by whf on 9/30/15.
 */
public class Bootstrap {
    public static void main(String[] args) {
        ApplicationContext ctx = initCtx("spring/spring-ctx.xml");

        Scheduler scheduler = (Scheduler) ctx.getBean("scheduleFactory");

/*        JobDetail jd = JobBuilder.newJob(TestJob.class)
                .withIdentity("test-job", "group-1")
                .build();


        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("test-trigger", "group-1")
                .withSchedule(
                        SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInSeconds(4)
                        .withRepeatCount(1)
                )
                .build();

        try {
            scheduler.scheduleJob(jd, trigger);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }*/


        ctx.getBean("sqlSessionFactory");
    }


    /**
     * 初始化Spring容器
     * @param conf
     * @return
     */
    public static ApplicationContext initCtx(String conf) {
        ApplicationContext ctx = new ClassPathXmlApplicationContext(conf);

        return ctx;
    }
}
