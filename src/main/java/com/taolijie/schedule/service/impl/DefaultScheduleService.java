package com.taolijie.schedule.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.taolijie.schedule.constant.Config;
import com.taolijie.schedule.constant.TaskStatus;
import com.taolijie.schedule.dao.mapper.schedule.TaskModelMapper;
import com.taolijie.schedule.exception.InvalidJobNameException;
import com.taolijie.schedule.job.DefaultRunOnceJob;
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
import java.util.Map;

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
    public void addJob(String host, Integer port, String path, String method, Date startAt, Map<String, String> parmMap)
            throws SchedulerException, InvalidJobNameException {

        // 创建job
        JobDataMap map = new JobDataMap();
        map.putAll(parmMap);
        map.put("callback.host", host);
        map.put("callback.port", port);
        map.put("callback.path", path);
        map.put("callback.method", method);

        saveTask(host, port, path, method, startAt, parmMap, map);

        JobDetail jd = makeJobDetail(parmMap.get("taskId"), map);

        // 创建trigger
        Trigger trigger = makeTrigger(parmMap.get("taskId"), startAt);

        // 启动调度
        scheduler.scheduleJob(jd, trigger);

        appLog.info("job added. id = {}, callbackPath = {}, startAt = {}", parmMap.get("taskId"), path, startAt);

    }

    @Override
    @Transactional(readOnly = true)
    public void loadJobs() throws SchedulerException {
        // 查出所有未执行的任务
        TaskModel example = new TaskModel();
        example.setStatus(TaskStatus.WAIT.code());
        List<TaskModel> taskList = taskMapper.findBy(example);

        for (TaskModel task : taskList) {
            loadSingleJob(task);
        }
    }

    /**
     * 根据TaskModel对象载入任务
     * @param task
     * @throws SchedulerException
     */
    private void loadSingleJob(TaskModel task) throws SchedulerException {
        // 反序列化参数数据
        JSONObject jsonO = JSON.parseObject(task.getParameter());

        // 创建job
        JobDataMap map = new JobDataMap();
        map.putAll(jsonO);
        map.put("callback.host", task.getCallbackHost());
        map.put("callback.port", task.getCallbackPort());
        map.put("callback.path", task.getCallbackPath());
        map.put("callback.method", task.getCallbackMethod());


        JobDetail jd = makeJobDetail(task.getId().toString(), map);

        // 创建trigger
        Trigger trigger = makeTrigger(task.getId().toString(), task.getExeAt());

        // 启动调度
        scheduler.scheduleJob(jd, trigger);
        appLog.info("job added. id = {}, callback = {}, startAt = {}", task.getId(), task.getCallbackPath(), task.getExeAt());

    }

    private JobDetail makeJobDetail(String id, JobDataMap map) {
        map.put("id", Integer.valueOf(id));

        return JobBuilder.newJob(DefaultRunOnceJob.class)
                .withIdentity(id, Config.JOB_GROUP)
                .setJobData(map)
                .build();
    }

    private Trigger makeTrigger(String id, Date exeAt) {
        return TriggerBuilder.newTrigger()
                .withIdentity(id, Config.TRIGGER_GROUP)
                .startAt(exeAt)
                .build();

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
    private void saveTask(String host, Integer port, String path, String method, Date exeAt, Map<String, String> parmMap, JobDataMap map) {
        TaskModel taskModel = new TaskModel();
        taskModel.setCreatedTime(new Date());
        taskModel.setStatus(TaskStatus.WAIT.code());

        taskModel.setTaskName(parmMap.get("taskId"));
        taskModel.setTaskGroup(Config.JOB_GROUP);

        taskModel.setTriggerName(parmMap.get("taskId"));
        taskModel.setTriggerGroup(Config.TRIGGER_GROUP);;

        taskModel.setExeAt(exeAt);
        taskModel.setCallbackHost(host);
        taskModel.setCallbackPort(port);
        taskModel.setCallbackPath(path);
        taskModel.setCallbackMethod(method);

        taskModel.setParameter(JSON.toJSONString(parmMap));
        taskMapper.insertSelective(taskModel);

        map.put("id", taskModel.getId());
    }
}
