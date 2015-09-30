package com.taolijie.schedule.service.impl;

import com.taolijie.schedule.constant.Config;
import com.taolijie.schedule.exception.InvalidJobNameException;
import com.taolijie.schedule.job.RunOnceJob;
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

/**
 * Created by whf on 9/30/15.
 */
@Service
public class DefaultScheduleService implements ScheduleService, ApplicationContextAware {
    public static Logger errLog = LoggerFactory.getLogger(Config.ERR_LOGGER);

    private static ApplicationContext ctx;

    @Autowired
    @Qualifier("scheduleFactory")
    private Scheduler scheduler;

    @Override
    public void addJob(String id, String jobBeanName, Date startAt)
            throws SchedulerException, InvalidJobNameException {

        String clazzName = StringUtils.concat(0, "com.taolijie.schedule.job.", jobBeanName);

        Class clazz = null;
        try {
            clazz = Class.forName(clazzName);
        } catch (ClassNotFoundException e) {
            errLog.error("invalid spring bean name:{}", clazzName);
            throw new InvalidJobNameException(clazzName);
        }


        JobDetail jd = JobBuilder.newJob(clazz)
                .withIdentity(id.toString(), Config.JOB_GROUP)
                .build();

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(id, Config.TRIGGER_GROUP)
                .startAt(startAt)
                .build();

        scheduler.scheduleJob(jd, trigger);
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
