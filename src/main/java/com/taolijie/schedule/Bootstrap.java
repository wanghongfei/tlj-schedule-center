package com.taolijie.schedule;

import com.taolijie.schedule.http.spark.SparkInitializer;
import com.taolijie.schedule.service.ScheduleService;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.FileOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.lang.management.ManagementFactory;

/**
 * 程序启动类
 * Created by whf on 9/30/15.
 */
public class Bootstrap {
    private static Logger log = LoggerFactory.getLogger(Bootstrap.class);
    private static Logger errLog = LoggerFactory.getLogger("ERROR-LOGGER");

    /**
     * web服务默认端口
     */
    public static final int DEF_PORT = 9000;

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

        log.info("Starting Spark");
        int port = getPortFromArgs(args);
        SparkInitializer sparkInit = loadSpark("sparkInitializer", ctx);
        sparkInit.start(port);

        try {
            writePid();
        } catch (IOException e) {
            errLog.error("writing PID to file [my-pid] Failed");
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
        // 载入错过的任务
        service.loadMissJob();
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

    private static SparkInitializer loadSpark(String beanName, ApplicationContext ctx) {
        return (SparkInitializer) ctx.getBean(beanName);
    }

    private static int getPortFromArgs(String[] args) {
        int port = DEF_PORT;
        if (args.length >= 1) {
            try {
                port = Integer.valueOf(args[0]);
            } catch (NumberFormatException ex) {
                log.info("invalid port [{}], using default:{}", args[0], DEF_PORT);
            }
        }

        return port;
    }

    /**
     * 把当前进行pid写入到文件中
     * @throws IOException
     */
    private static void writePid() throws IOException {
        String name = ManagementFactory.getRuntimeMXBean().getName();
        String pid = name.split("@")[0];

        FileOutputStream fos = new FileOutputStream("my-pid");
        fos.write(pid.getBytes());
        fos.close();
    }
}
