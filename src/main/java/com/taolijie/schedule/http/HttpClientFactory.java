package com.taolijie.schedule.http;

import com.taolijie.schedule.constant.Config;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 使用了连接池的HttpClient工厂
 * Created by whf on 10/7/15.
 */
public class HttpClientFactory {
    private static Logger infoLog = LoggerFactory.getLogger(Config.APP_LOGGER);

    private static PoolingHttpClientConnectionManager pool;

    private static HttpClientBuilder builder;

    static {
        infoLog.info("creating HTTP connection pool");

        pool = new PoolingHttpClientConnectionManager();
        pool.setDefaultMaxPerRoute(20);
        pool.setMaxTotal(50);

        builder = HttpClientBuilder.create();
        builder.setConnectionManager(pool);

        infoLog.info("done creating HTTP connection pool");
    }

    public static HttpClient getClient() {
        return builder.build();
    }
}
