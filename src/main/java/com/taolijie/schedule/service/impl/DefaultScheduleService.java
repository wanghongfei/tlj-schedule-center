package com.taolijie.schedule.service.impl;

import com.taolijie.schedule.constant.Config;
import com.taolijie.schedule.constant.TaskStatus;
import com.taolijie.schedule.dao.mapper.schedule.TaskModelMapper;
import com.taolijie.schedule.exception.InvalidJobNameException;
import com.taolijie.schedule.model.TaskModel;
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
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    private TaskModelMapper taskMapper;

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

        saveTask(id.toString(), jobBeanName, startAt, map);
    }

    @Override
    public void delJob(String id) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey(id, Config.JOB_GROUP);
        TriggerKey triKey = TriggerKey.triggerKey(id, Config.TRIGGER_GROUP);

        scheduler.pauseTrigger(triKey);
        boolean delResult = scheduler.deleteJob(jobKey);

        if (delResult) {
            appLog.info("job deleted: name = {}, group = {}", jobKey.getName(), jobKey.getGroup());
        } else {
            appLog.info("job failed to be deleted: name = {}, group = {}", jobKey.getName(), jobKey.getGroup());
        }

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.ctx = applicationContext;
    }

    /**
     * 任务信息保存到数据库中
     */
    @Transactional(readOnly = false)
    private void saveTask(String id, String beanName, Date exeAt, JobDataMap map) {
        TaskModel taskModel = new TaskModel();
        taskModel.setCreatedTime(new Date());
        taskModel.setStatus(TaskStatus.WAIT.code());

        taskModel.setTaskName(id.toLowerCase());
        taskModel.setTaskGroup(Config.JOB_GROUP);

        taskModel.setTriggerName(id.toLowerCase());
        taskModel.setTriggerGroup(Config.TRIGGER_GROUP);;

        taskModel.setBeanName(beanName);
        taskModel.setExeAt(exeAt);
        taskMapper.insertSelective(taskModel);

        map.put("id", taskModel.getId());
    }
}
