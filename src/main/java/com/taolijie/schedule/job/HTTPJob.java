package com.taolijie.schedule.job;

import com.alibaba.fastjson.JSON;
import com.taolijie.schedule.component.ResponseText;
import com.taolijie.schedule.constant.Config;
import com.taolijie.schedule.http.HttpClientFactory;
import com.taolijie.schedule.util.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * 调用HTTP接口的Job
 * Created by whf on 11/2/15.
 */
public abstract class HTTPJob implements Job {

    protected static Logger infoLogger = LoggerFactory.getLogger(Config.APP_LOGGER);
    protected static Logger errLogger = LoggerFactory.getLogger(Config.ERR_LOGGER);

    /**
     * 子类实现此方法来处理任务执行过程中的异常
     * @param ex
     */
    protected abstract void onException(JobExecutionContext ctx, Exception ex);

    /**
     * 该方法在任务正常执行完成后被调用
     * @param ctx
     */
    protected abstract void afterExecution(JobExecutionContext ctx);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            doJob(context);

        } catch (Exception ex) {
            onException(context, ex);

        }

        afterExecution(context);
    }


    protected void doJob(JobExecutionContext context) throws JobExecutionException {

        // 取出接口参数
        JobDataMap map = context.getJobDetail().getJobDataMap();
        String host = map.getString("callback.host");
        int port = map.getInt("callback.port");
        String path = map.getString("callback.path");
        String method = map.getString("callback.method");

        // 调用远程接口
        HttpClient client = HttpClientFactory.getClient();
        if (method.equals("GET")) {
            doGet(host, port, path, map);
        }
    }

    /**
     * 发起GET请求
     * @throws JobExecutionException
     */
    private void doGet(String host, int port, String path, JobDataMap map) throws JobExecutionException {
        // 生成URL
        String url = StringUtils.concat(0, "http://", host, ":", port, path);

        // 生成带参数的URL
        String fullUrl = StringUtils.concat(40, url, genQueryString(map));
        if (infoLogger.isDebugEnabled()) {
            infoLogger.debug("sending request to {}", fullUrl);
        }

        // 发起请求
        HttpClient client = HttpClientFactory.getClient();
        HttpGet GET = new HttpGet(fullUrl);
        try {
            HttpResponse resp = client.execute(GET);
            doResponse(resp, fullUrl);

        } catch (IOException e) {
            errLogger.error(e.toString());
            throw new JobExecutionException();
        }

    }

    /**
     * 处理返回数据
     * @throws JobExecutionException
     * @throws IOException
     */
    private void doResponse(HttpResponse resp, String fullUrl) throws JobExecutionException, IOException {
        String str = StringUtils.stream2String(resp.getEntity().getContent());

        if (infoLogger.isDebugEnabled()) {
            infoLogger.debug("callback returned: {}", str);
        }
        // 解码返回值
        ResponseText rt = JSON.parseObject(str, ResponseText.class);
        if (rt.isOk()) {
            // 执行成功
            if (infoLogger.isDebugEnabled()) {
                infoLogger.debug("callback request to {} succeeded", fullUrl);
            }

        } else {
            // 执行出错
            infoLogger.error("callback request to {} failed!", fullUrl);
            throw new JobExecutionException("");
        }

    }

    /**
     * 生成GET请求参数
     * @param map
     * @return
     */
    private String genQueryString(JobDataMap map) {
        StringBuilder sb = new StringBuilder(20);
        sb.append("?");

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            if (key.startsWith("callback")) {
                continue;
            }

            Object val = entry.getValue();
            sb.append(key);
            sb.append("=");
            sb.append(val.toString());
            sb.append("&");
        }

        return sb.toString();
    }

}
