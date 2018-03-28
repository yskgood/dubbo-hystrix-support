package com.netease.dubbo.service;

/**
 * Created by yangshaokai on 2018/2/7.
 */
public interface EchoService {

    String echo();

    String echoWithTimeOut();

    String echoWithException();

}
