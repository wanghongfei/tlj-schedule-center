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
}
