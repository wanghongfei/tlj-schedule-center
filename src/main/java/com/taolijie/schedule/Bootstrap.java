package com.taolijie.schedule;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by whf on 9/30/15.
 */
public class Bootstrap {
    public static void main(String[] args) {
        ApplicationContext ctx = initCtx("spring/spring-ctx.xml");
    }

    public static ApplicationContext initCtx(String conf) {
        ApplicationContext ctx = new ClassPathXmlApplicationContext(conf);

        return ctx;
    }
}
