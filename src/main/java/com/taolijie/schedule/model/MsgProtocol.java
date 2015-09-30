package com.taolijie.schedule.model;

import java.util.Date;

/**
 * 通讯协议
 * Created by whf on 9/30/15.
 */
public class MsgProtocol {
    /**
     * 消息的类型
     */
    private int type;

    /**
     * job的spring bean名
     */
    private String beanName;

    /**
     * crontab表达式
     */
    private String cronExp;

    private Date exeAt;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public String getCronExp() {
        return cronExp;
    }

    public void setCronExp(String cronExp) {
        this.cronExp = cronExp;
    }

    public Date getExeAt() {
        return exeAt;
    }

    public void setExeAt(Date exeAt) {
        this.exeAt = exeAt;
    }
}
