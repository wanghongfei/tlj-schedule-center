package com.taolijie.schedule.http.spark.service;

import com.taolijie.schedule.dao.mapper.schedule.TaskModelMapper;
import com.taolijie.schedule.http.spark.model.JobModel;
import com.taolijie.schedule.http.spark.model.JsonList;
import com.taolijie.schedule.model.TaskModel;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by whf on 11/3/15.
 */
@Service
public class MonitorService {
    @Autowired
    private Scheduler scheduler;

    @Autowired
    private TaskModelMapper taskMapper;

    /**
     * 得到当前正在调度的job
     * @throws SchedulerException
     */
    public JsonList<JobModel> getJobList() throws SchedulerException {
        Set<JobKey> keySet = scheduler.getJobKeys(GroupMatcher.anyJobGroup());

        List<JobModel> jobList = new ArrayList<>(keySet.size() + 3);

        for (JobKey key : keySet) {
            List<? extends Trigger> triggers = scheduler.getTriggersOfJob(key);

            // 每个job只有一个trigger
            Trigger trigger = triggers.get(0);

            JobModel model = new JobModel();
            model.setJobName(key.getName());
            model.setJobGroup(key.getGroup());
            model.setStatus(scheduler.getTriggerState(trigger.getKey()).name());
            model.setExeAt(trigger.getNextFireTime());

            if (trigger instanceof CronTrigger) {
                CronTrigger cronTrigger = (CronTrigger) trigger;
                model.setCronExp(cronTrigger.getCronExpression());
            }

            jobList.add(model);
        }

        return new JsonList<>(jobList);
    }

    /**
     * 查询任务记录(不包含crontab任务)
     * @param start
     * @param end
     * @return
     */
    public JsonList<TaskModel> getHistory(Date start, Date end) {
        List<TaskModel> list = taskMapper.selectByInterval(start, end);

        return new JsonList<>(list);
    }
}
