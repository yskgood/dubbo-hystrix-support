package com.netease.dubbo;

import com.netease.dubbo.service.EchoService;
import com.netflix.config.ConfigurationManager;
import com.netflix.hystrix.HystrixThreadPoolKey;
import com.netflix.hystrix.HystrixThreadPoolProperties;
import com.netflix.hystrix.strategy.properties.HystrixPropertiesFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.concurrent.TimeUnit;

/**
 * Created by yangshaokai on 2018/2/7.
 */
public class ConsumerTest {

    EchoService echoService;

    @Before
    public void before() {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"consumer.xml"});
        context.start();
        echoService = (EchoService) context.getBean("echoService", EchoService.class);
    }

    @Test
    public void test() {
        Assert.assertEquals("Hello World", echoService.echo());

    }

    @Test
    public void testTimeout() {
        Assert.assertEquals("Hello World", echoService.echoWithTimeOut());
    }

    @Test
    public void testEchoException() {
        echoService.echoWithException();
    }
}
