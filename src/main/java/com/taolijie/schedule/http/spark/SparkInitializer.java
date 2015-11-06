package com.taolijie.schedule.http.spark;

import com.alibaba.fastjson.JSON;
import com.taolijie.schedule.http.spark.service.MonitorService;
import com.taolijie.schedule.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import spark.Spark;


import java.text.ParseException;
import java.util.Date;

import static spark.Spark.*;

/**
 * Created by whf on 11/3/15.
 */
@Component
public class SparkInitializer {
    @Autowired
    private MonitorService monitorService;

    public void start(int port) {
        Spark.port(port);
        Spark.threadPool(5);


        initController();
    }

    private void initController() {
        // 查询当前正在调度的任务
        get("/status", (req, res) -> {
            res.type("application/json");
            return monitorService.getJobList();

        }, JSON::toJSONString);

        // 查询任务记录
        get("/history", (req, res) -> {
            String parmStart = req.params("start");
            String parmEnd = req.params("end");

            try {
                Date start = StringUtils.str2Date(parmStart);
                Date end = StringUtils.str2Date(parmEnd);
                return monitorService.getHistory(start, end);

            } catch (ParseException ex) {
                res.status(400);
                return "error";
            }

        });
    }
}
