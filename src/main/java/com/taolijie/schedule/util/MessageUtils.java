package com.taolijie.schedule.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.taolijie.schedule.exception.InvalidMessageException;
import com.taolijie.schedule.model.MsgProtocol;

/**
 * Created by whf on 9/30/15.
 */
public class MessageUtils {
    private MessageUtils() {}

    /**
     * 解码消息
     * @param buf
     * @return
     * @throws InvalidMessageException
     */
    public static MsgProtocol deserailize(byte[] buf) throws InvalidMessageException {
        try {
            return JSON.parseObject(buf, MsgProtocol.class);

        } catch (JSONException e) {
            throw new InvalidMessageException();
        }
    }
}
