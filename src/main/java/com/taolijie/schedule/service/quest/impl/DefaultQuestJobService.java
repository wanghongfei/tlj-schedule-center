package com.taolijie.schedule.service.quest.impl;

import com.taolijie.schedule.dao.mapper.QuestAssignModelMapper;
import com.taolijie.schedule.model.QuestAssignModel;
import com.taolijie.schedule.service.quest.QuestJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by whf on 9/30/15.
 */
@Service
public class DefaultQuestJobService implements QuestJobService {
    @Autowired
    private QuestAssignModelMapper assignMapper;

    @Override
    @Transactional(readOnly = false)
    public void questAssignExpired(Integer assignId, String newStatus) {
        QuestAssignModel example = new QuestAssignModel();
        example.setId(assignId);
        example.setStatus(newStatus);

        assignMapper.updateByPrimaryKeySelective(example);
    }
}
