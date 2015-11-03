package com.taolijie.schedule.http.spark.model;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

/**
 * Created by whf on 11/3/15.
 */
public class JobModel {
    private String jobName;
    private String jobGroup;
    private String trigger;

    private String status;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date exeAt;

    private String cronExp;

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobGroup() {
        return jobGroup;
    }

    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
    }

    public String getTrigger() {
        return trigger;
    }

    public void setTrigger(String trigger) {
        this.trigger = trigger;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getExeAt() {
        return exeAt;
    }

    public void setExeAt(Date exeAt) {
        this.exeAt = exeAt;
    }

    public String getCronExp() {
        return cronExp;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("JobModel{");
        sb.append("jobName='").append(jobName).append('\'');
        sb.append(", jobGroup='").append(jobGroup).append('\'');
        sb.append(", trigger='").append(trigger).append('\'');
        sb.append(", status='").append(status).append('\'');
        sb.append(", exeAt=").append(exeAt);
        sb.append(", cronExp='").append(cronExp).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public void setCronExp(String cronExp) {
        this.cronExp = cronExp;
    }
}
