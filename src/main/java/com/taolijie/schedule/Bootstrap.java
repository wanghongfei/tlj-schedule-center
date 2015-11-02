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

import java.io.IOException;
import java.util.Date;

/**
 * 程序启动类
 * Created by whf on 9/30/15.
 */
public class Bootstrap {
    private static Logger log = LoggerFactory.getLogger("INFO-LOGGER");
    private static Logger errLog = LoggerFactory.getLogger("ERROR-LOGGER");

    public static void main(String[] args) {
        ApplicationContext ctx = initCtx("spring/spring-ctx.xml");
        log.info("Spring started");

        // 载入没有执行的任务
        log.info("loading jobs... ...");
        try {
            loadJobs(ctx);
        } catch (Exception e) {
            e.printStackTrace();
            errLog.error("job loading failed!!");
        }

        log.info("all done. ^_^|");

    }

    /**
     * 载入没有执行的任务
     * @param ctx
     * @throws SchedulerException
     */
    private static void loadJobs(ApplicationContext ctx) throws SchedulerException, IOException {
        ScheduleService service = (ScheduleService) ctx.getBean("defaultScheduleService");
        // 从数据库中读取上次没有执行完成的任务
        service.loadJobs();
        // 载入日常任务
        service.loadRoutineJob();
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


}
