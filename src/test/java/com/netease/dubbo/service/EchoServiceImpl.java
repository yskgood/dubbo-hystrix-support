package com.netease.dubbo.service;

import java.util.concurrent.TimeUnit;

/**
 * Created by yangshaokai on 2018/2/7.
 */
public class EchoServiceImpl implements EchoService {

    @Override
    public String echo() {
        return "Hello World";
    }

    @Override
    public String echoWithTimeOut() {
        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "Hello World";
    }

    @Override
    public String echoWithException() {
        return String.format("%s", 10 / 0);
    }
}
