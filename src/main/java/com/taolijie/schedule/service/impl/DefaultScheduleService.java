package com.taolijie.schedule.service.impl;

import com.taolijie.schedule.constant.Config;
import com.taolijie.schedule.exception.InvalidJobNameException;
import com.taolijie.schedule.service.ScheduleService;
import com.taolijie.schedule.util.StringUtils;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by whf on 9/30/15.
 */
@Service
public class DefaultScheduleService implements ScheduleService, ApplicationContextAware {
    public static Logger errLog = LoggerFactory.getLogger(Config.ERR_LOGGER);
    public static Logger appLog = LoggerFactory.getLogger(Config.APP_LOGGER);

    private static ApplicationContext ctx;

    @Autowired
    @Qualifier("scheduleFactory")
    private Scheduler scheduler;

    /**
     * 添加并激活一个任务.
     *
     * @throws SchedulerException 任务调度出错
     * @throws InvalidJobNameException job bean不存在
     */
    @Override
    public void addJob(String id, String jobBeanName, Date startAt, List<Object> parmList)
            throws SchedulerException, InvalidJobNameException {

        // 根据beanName参数生成目标类的全限定名
        String clazzName = StringUtils.concat(0, "com.taolijie.schedule.job.", jobBeanName);

        // 得到该类的class对象
        Class clazz = null;
        try {
            clazz = Class.forName(clazzName);
        } catch (ClassNotFoundException e) {
            errLog.error("invalid spring bean name:{}", clazzName);
            throw new InvalidJobNameException(clazzName);
        }


        // 创建job
        JobDataMap map = new JobDataMap();
        map.put("parm", parmList);

        JobDetail jd = JobBuilder.newJob(clazz)
                .withIdentity(id.toString(), Config.JOB_GROUP)
                .setJobData(map)
                .build();

        // 创建trigger
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(id, Config.TRIGGER_GROUP)
                .startAt(startAt)
                .build();

        // 启动调度
        scheduler.scheduleJob(jd, trigger);

        appLog.info("job added. id = {}, beanName = {}, startAt = {}", id, jobBeanName, startAt);
    }

    @Override
    public void delJob(String id) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey(id, Config.JOB_GROUP);
        TriggerKey triKey = TriggerKey.triggerKey(id, Config.TRIGGER_GROUP);

        scheduler.pauseTrigger(triKey);
        scheduler.unscheduleJob(triKey);
        scheduler.deleteJob(jobKey);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.ctx = applicationContext;
    }
}
