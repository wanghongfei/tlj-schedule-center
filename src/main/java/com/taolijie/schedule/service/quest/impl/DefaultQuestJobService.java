package com.taolijie.schedule.service.quest.impl;

import com.alibaba.fastjson.JSON;
import com.taolijie.schedule.constant.*;
import com.taolijie.schedule.dao.mapper.tlj.FinishReqModelMapper;
import com.taolijie.schedule.dao.mapper.tlj.QuestAssignModelMapper;
import com.taolijie.schedule.model.FinishReqModel;
import com.taolijie.schedule.model.MsgProtocol;
import com.taolijie.schedule.model.QuestAssignModel;
import com.taolijie.schedule.service.quest.QuestJobService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

/**
 * Created by whf on 9/30/15.
 */
@Service
public class DefaultQuestJobService implements QuestJobService {
    private static final Logger appLog = LoggerFactory.getLogger(Config.APP_LOGGER);

    @Autowired
    private QuestAssignModelMapper assignMapper;

    @Autowired
    private FinishReqModelMapper fiMapper;

    @Autowired
    @Qualifier("redisTemplateForString")
    private RedisTemplate<String, String> rt;



    @Override
    @Transactional(readOnly = false)
    public void questAssignExpired(Integer assignId, String newStatus) {
        // 检查任务领取表的状态是不是"03:已经提交"
        // 如果是，则不进行任何操作，方法直接返回
        QuestAssignModel model = assignMapper.selectByPrimaryKey(assignId);
        if (model.getStatus().equals("03")) {
            return;
        }

        // 修改状态
        QuestAssignModel example = new QuestAssignModel();
        example.setId(assignId);
        example.setStatus(newStatus);

        assignMapper.updateByPrimaryKeySelective(example);
    }

    @Override
    public void autoAuditNotify(Integer reqId) {
        appLog.info("autoAuditNotify executed");

        // 检查任务状态
        FinishReqModel model = fiMapper.selectByPrimaryKey(reqId);
        // 如果状态是最终状态, 则不执行任何操作
        RequestStatus st = RequestStatus.fromCode(model.getStatus());
        if (RequestStatus.isFinalStatus(st)) {
            appLog.info("status is {}, do nothing.", st.code());
            return;
        }


        rt.execute((RedisConnection redisConn) -> {
            StringRedisConnection conn = (StringRedisConnection) redisConn;

            // 构造信息体
            MsgProtocol msg = new MsgProtocol();
            msg.setType(MsgType.AUTO_AUDIT.code());
            // 构造参数
            msg.setParmList(Arrays.asList(reqId));

            // 序列化
            String json = JSON.toJSONString(msg);
            if (appLog.isDebugEnabled()) {
                appLog.debug("sending message to channel[{}], content = {}", RedisChannel.AUTO_AUDIT.code(), json);
            }

            // 发布消息
            conn.publish(RedisChannel.AUTO_AUDIT.code(), json);

            return null;
        });

    }
}
