package com.taolijie.schedule.service.quest;

/**
 * Created by whf on 9/30/15.
 */
public interface QuestJobService {
    /**
     * 让任务领取过期
     * @param assignId
     */
    void questAssignExpired(Integer assignId, String newStatus);

    /**
     * 自动通过任务提交申请.
     * 这个方法并不实际执行业务，而是向redis投递一条消息
     */
    void autoAuditNotify(Integer reqId);
}
