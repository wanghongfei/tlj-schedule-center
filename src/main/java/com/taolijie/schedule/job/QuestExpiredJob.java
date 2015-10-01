package com.taolijie.schedule.job;

import com.taolijie.schedule.constant.Config;
import com.taolijie.schedule.service.quest.QuestJobService;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Job内容: 领取任务后2小时内如果没有提交任务，
 * 修改状态为未领取
 * Created by whf on 9/30/15.
 */
@Component
public class QuestExpiredJob extends RunOnceJob  {
    public static Logger appLog = LoggerFactory.getLogger(Config.APP_LOGGER);

    @Override
    protected void doJob(JobExecutionContext context) throws JobExecutionException {
        appLog.info("executing job: QuestExpiredJob");

        // 取出参数
        JobDataMap map = context.getJobDetail().getJobDataMap();
        List<Object> parmList = (List<Object>) map.get("parm");

        QuestJobService service = (QuestJobService) ctx.getBean("defaultQuestJobService");
        Integer assignId = (Integer) parmList.get(0);
        String status = (String) parmList.get(1);

        service.questAssignExpired(assignId, status);

        appLog.info("done executing job: QuestExpiredJob");
    }
}
