package com.taolijie.schedule.service;

import java.util.Date;

/**
 * Created by whf on 9/30/15.
 */
public interface ScheduleService {
    void addJob(String jobBeanName, Date startAt);
}
