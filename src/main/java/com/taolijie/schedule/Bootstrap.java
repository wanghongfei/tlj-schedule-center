package com.taolijie.schedule;

import com.taolijie.schedule.service.ScheduleService;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Date;

/**
 * 程序启动类
 * Created by whf on 9/30/15.
 */
public class Bootstrap {
    private static Logger log = LoggerFactory.getLogger("LOGGER");

    public static void main(String[] args) {
        ApplicationContext ctx = initCtx("spring/spring-ctx.xml");

        log.info("Spring started");

        //ScheduleService service = (ScheduleService) ctx.getBean("defaultScheduleService");

/*        try {
            service.addJob("1", "QuestExpiredJob", new Date());

        } catch (SchedulerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }*/


/*        JobDetail jd = JobBuilder.newJob(QuestExpiredJob.class)
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


    }


    /**
     * 初始化Spring容器
     * @param conf
     * @return
     */
    private static ApplicationContext initCtx(String conf) {
        ApplicationContext ctx = new ClassPathXmlApplicationContext(conf);

        return ctx;
    }

    /**
     * @deprecated
     */
    private static void subscribeChannel(ApplicationContext ctx, String chan) {
        StringRedisTemplate rt = (StringRedisTemplate)ctx.getBean("redisTemplateForString");

        rt.execute((RedisConnection redisConnection) -> {
            StringRedisConnection strConn = (StringRedisConnection) redisConnection;

            strConn.subscribe((message, bytes) -> {
                System.out.println(message.toString());
            }, "chan");

            log.info("subscribed channel: {}", chan);

            return null;
        });

    }

    private static void triggerInit(ApplicationContext ctx) {
        Scheduler scheduler = (Scheduler) ctx.getBean("scheduleFactory");
        ctx.getBean("sqlSessionFactory");

        StringRedisTemplate rt = (StringRedisTemplate)ctx.getBean("redisTemplateForString");

    }
}
