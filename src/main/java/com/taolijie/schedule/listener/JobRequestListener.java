package com.taolijie.schedule.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.taolijie.schedule.constant.Config;
import com.taolijie.schedule.exception.InvalidMessageException;
import com.taolijie.schedule.model.MsgProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

/**
 * Created by whf on 9/30/15.
 */
public class JobRequestListener extends MessageListenerAdapter {
    private static final Logger appLog = LoggerFactory.getLogger(Config.APP_LOGGER);
    private static final Logger errLog = LoggerFactory.getLogger(Config.APP_LOGGER);

    @Override
    public void onMessage(Message message, byte[] pattern) {
        if (appLog.isDebugEnabled()) {
            appLog.debug("received new message: {}", message.toString());
        }

        String str = message.toString();

        // 解码
        try {
            MsgProtocol msg = JSON.parseObject(str, MsgProtocol.class);
        } catch (JSONException ex) {
            // 解码失败
            errLog.error("decoding failed for: {}", str);
        }
    }
}
