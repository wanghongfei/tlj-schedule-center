package com.taolijie.schedule.test;

import com.taolijie.schedule.http.HttpClientFactory;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by whf on 10/7/15.
 */
public class HttpTest {
    @Test
    public void test() throws Exception {

        HttpClientBuilder builder = HttpClientBuilder.create();
        for (int ix = 0 ; ix < 1 ; ++ix) {
            //sendOneQuest(HttpClientFactory.getClient());
            //sendOneQuest(builder.build());
        }
    }

}
