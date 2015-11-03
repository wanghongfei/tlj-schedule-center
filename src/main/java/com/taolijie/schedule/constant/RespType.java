package com.taolijie.schedule.constant;

/**
 * Created by whf on 11/3/15.
 */
public enum RespType {
    JSON("application/json;charset=utf-8");

    private String code;
    private RespType(String code) {
        this.code = code;
    }
}
