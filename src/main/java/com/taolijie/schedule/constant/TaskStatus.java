package com.taolijie.schedule.constant;

/**
 * Created by whf on 10/4/15.
 */
public enum TaskStatus {
    WAIT(0),
    EXECUTING(1),
    COMPLETED(2),
    ERROR(3);

    private int code;

    private TaskStatus(int code) {
        this.code = code;
    }

    public int code() {
        return this.code;
    }
}
