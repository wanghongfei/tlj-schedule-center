package com.taolijie.schedule.job;

import com.taolijie.schedule.constant.TaskStatus;
import com.taolijie.schedule.dao.mapper.schedule.TaskModelMapper;
import com.taolijie.schedule.model.TaskModel;
import com.taolijie.schedule.service.ScheduleService;
import org.quartz.*;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.transaction.annotation.Transactional;

/**
 * 只执行一次的Job. Job执行完成后会自动删除自身。
 * Created by whf on 9/30/15.
 */
public abstract class RunOnceJob implements Job, ApplicationContextAware {
    protected static ApplicationContext ctx;

    private static ScheduleService scheduleService;
    private static TaskModelMapper taskMapper;



    /**
     * 子类重写此方法，定义自己的业务逻辑
     * @param context
     * @throws JobExecutionException
     */
    abstract protected void doJob(JobExecutionContext context) throws JobExecutionException;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            doJob(context);
            markFinish(context.getJobDetail());

        } catch (JobExecutionException e) {
            // 任务执行出错
            markError(context.getJobDetail());
        }


        // 执行完成后马上删除任务本身
        try {
            delJob(context.getJobDetail());
        } catch (SchedulerException e) {
            throw new JobExecutionException("delete job failed");
        }
    }



    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ctx = applicationContext;
    }

    /**
     * 标记任务状态为出错
     * @param jd
     */
    private void markError(JobDetail jd) {
        Integer id = (Integer) jd.getJobDataMap().get("id");

        if (null == taskMapper) {
            taskMapper = (TaskModelMapper) ctx.getBeansOfType(TaskModelMapper.class);
        }


        TaskModel example = new TaskModel();
        example.setId(id);
        example.setStatus(TaskStatus.ERROR.code());
        taskMapper.updateByPrimaryKeySelective(example);

    }

    /**
     * 标记任务状态为已经完成
     * @param jd
     */
    @Transactional(readOnly = false)
    private void markFinish(JobDetail jd) {
        Integer id = (Integer) jd.getJobDataMap().get("id");

        if (null == taskMapper) {
            taskMapper = (TaskModelMapper) ctx.getBeansOfType(TaskModelMapper.class);
        }


        TaskModel example = new TaskModel();
        example.setId(id);
        example.setStatus(TaskStatus.COMPLETED.code());
        taskMapper.updateByPrimaryKeySelective(example);
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
}
