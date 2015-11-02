package com.taolijie.schedule.job;

import org.quartz.JobExecutionContext;

/**
 * 日常任务crontab风格
 * Created by whf on 11/2/15.
 */
public class RoutineHTTPJob extends HTTPJob {
    @Override
    protected void onException(JobExecutionContext ctx, Exception ex) {

    }

    @Override
    protected void afterExecution(JobExecutionContext ctx) {

    }
}
