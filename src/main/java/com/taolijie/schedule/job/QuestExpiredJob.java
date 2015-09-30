package com.taolijie.schedule.job;

import org.quartz.*;
import org.springframework.stereotype.Component;

/**
 * Job内容: 领取任务后2小时内如果没有提交任务，
 * 修改状态为未领取
 * Created by whf on 9/30/15.
 */
@Component
public class QuestExpiredJob extends RunOnceJob  {
    @Override
    protected void doJob(JobExecutionContext context) throws JobExecutionException {
        System.out.println("hello");
    }
}
