package com.taolijie.schedule.job;

import com.taolijie.schedule.service.quest.QuestJobService;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by whf on 9/30/15.
 */
@Component
public class AutoAuditJob extends RunOnceJob {

    @Override
    protected void doJob(JobExecutionContext context) throws JobExecutionException {
        // 取出参数
        JobDataMap map = context.getJobDetail().getJobDataMap();
        List<Object> parmList = (List<Object>) map.get("parm");

        QuestJobService service = (QuestJobService) ctx.getBean("defaultQuestJobService");
        Integer reqId = (Integer) parmList.get(0);
        service.autoAuditNotify(reqId);

    }
}
