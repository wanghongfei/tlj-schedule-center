package com.taolijie.schedule.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.taolijie.schedule.constant.Config;
import com.taolijie.schedule.constant.MsgType;
import com.taolijie.schedule.exception.InvalidJobNameException;
import com.taolijie.schedule.exception.InvalidMessageException;
import com.taolijie.schedule.model.MsgProtocol;
import com.taolijie.schedule.service.ScheduleService;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.stereotype.Component;

/**
 * Created by whf on 9/30/15.
 */
@Component
public class JobRequestListener extends MessageListenerAdapter {
    private static final Logger appLog = LoggerFactory.getLogger(Config.APP_LOGGER);
    private static final Logger errLog = LoggerFactory.getLogger(Config.APP_LOGGER);

    @Autowired
    private ScheduleService scheduleService;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        if (appLog.isDebugEnabled()) {
            appLog.debug("received new message: {}", message.toString());
        }

        String str = message.toString();

        // 解码
        try {
            MsgProtocol msg = JSON.parseObject(str, MsgProtocol.class);
            dispatchMessage(msg);
        } catch (JSONException ex) {
            // 解码失败
            errLog.error("decoding failed for: {}", str);
        } catch (InvalidJobNameException ex) {
            errLog.error("invalid job name: {}", ex.getMessage());
        } catch (SchedulerException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 根据消息类型分发请求
     * @param msg
     * @throws InvalidJobNameException
     * @throws SchedulerException
     */
    private void dispatchMessage(MsgProtocol msg) throws InvalidJobNameException, SchedulerException {
        // 取出参数
        // 先取出消息类型
        MsgType type = MsgType.fromCode(msg.getType());
        switch (type) {
            case DATE_STYLE:
                scheduleService.addJob(
                        msg.getCallbackHost(),
                        msg.getCallbackPort(),
                        msg.getCallbackPath(),
                        msg.getCallbackMethod(),
                        msg.getExeAt(),
                        msg.getParmMap());

                break;


            default:
                errLog.error("invalid message type:{}", msg.getType());

        }


    }
}
