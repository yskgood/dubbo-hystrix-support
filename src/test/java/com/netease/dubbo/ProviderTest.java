package com.netease.dubbo;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by yangshaokai on 2018/2/7.
 */
public class ProviderTest {

    @Test
    public void test() throws Exception {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"provider.xml"});
        context.start();

        System.in.read(); // 按任意键退出
        context.close();

    }
}
