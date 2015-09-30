package com.taolijie.schedule.constant;

/**
 * Created by whf on 9/30/15.
 */
public enum RedisChannel {
    POST_JOB("post-job"),
    AUTO_AUDIT("auto-audit");

    private String code;

    private RedisChannel(String code) {
        this.code = code;
    }

    public String code() {
        return this.code;
    }
}
