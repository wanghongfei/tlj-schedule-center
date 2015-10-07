package com.taolijie.schedule.http;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

/**
 * 使用了连接池的HttpClient工厂
 * Created by whf on 10/7/15.
 */
public class HttpClientFactory {
    private static PoolingHttpClientConnectionManager pool;

    private static HttpClientBuilder builder;

    static {
        pool = new PoolingHttpClientConnectionManager();
        pool.setDefaultMaxPerRoute(20);
        pool.setMaxTotal(50);

        builder = HttpClientBuilder.create();
        builder.setConnectionManager(pool);
    }

    public static HttpClient getClient() {
        return builder.build();
    }
}
