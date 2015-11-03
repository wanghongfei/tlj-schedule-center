package com.taolijie.schedule.http.spark;

import com.taolijie.schedule.http.spark.service.MonitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import spark.Spark;
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
        get("/status", (req, res) -> {
            return monitorService.getJobList();
        });
    }
}
