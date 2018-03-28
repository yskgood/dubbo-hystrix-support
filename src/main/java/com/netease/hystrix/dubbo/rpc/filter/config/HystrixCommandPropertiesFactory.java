package com.netease.hystrix.dubbo.rpc.filter.config;

import com.alibaba.dubbo.common.URL;
import com.netflix.hystrix.HystrixCommandProperties;

/**
 * 命令相关配置
 * Created by yangshaokai on 2018/2/28.
 */
public class HystrixCommandPropertiesFactory {

    public static HystrixCommandProperties.Setter create(URL url) {
        return HystrixCommandProperties.Setter().withCircuitBreakerSleepWindowInMilliseconds(url.getParameter("sleepWindowInMilliseconds", 5000))
                .withCircuitBreakerErrorThresholdPercentage(url.getParameter("errorThresholdPercentage", 50))
                .withCircuitBreakerRequestVolumeThreshold(url.getParameter("requestVolumeThreshold", 20))
                .withExecutionIsolationThreadInterruptOnTimeout(true)
                .withExecutionTimeoutInMilliseconds(url.getParameter("timeoutInMilliseconds", 1000));
    }
}
