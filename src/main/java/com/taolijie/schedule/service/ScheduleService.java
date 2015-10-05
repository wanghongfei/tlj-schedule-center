package com.taolijie.schedule.service;

import com.taolijie.schedule.exception.InvalidJobNameException;
import org.quartz.SchedulerException;

import java.util.Date;
import java.util.List;

/**
 * Created by whf on 9/30/15.
 */
public interface ScheduleService {
    /**
     * 添加一个任务
     *
     * @param id 任务唯一标识
     * @param jobBeanName 执行任务的springBean
     * @param startAt 执行时间点
     *
     * @exception SchedulerException 调度出错
     * @exception InvalidJobNameException job bean不存在
     */
    void addJob(String id, String jobBeanName, Date startAt, List<Object> parmList)
            throws SchedulerException, InvalidJobNameException;

    /**
     * 从数据库中载入还没有被执行的任务
     */
    void loadJobs()
            throws SchedulerException;

    /**
     * 停止并删除一个任务
     *
     * @param id 任务唯一标识
     * @throws SchedulerException
     */
    void delJob(String id)
            throws SchedulerException;
}
