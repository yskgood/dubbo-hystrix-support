package com.netease.hystrix.dubbo.rpc.filter.config;

import com.alibaba.dubbo.common.URL;
import com.netflix.hystrix.HystrixCommandProperties;

/**
 * 命令相关配置
 * Created by yangshaokai on 2018/2/28.
 */
public class HystrixCommandPropertiesFactory {

    public static HystrixCommandProperties.Setter create(URL url, String method) {
        return HystrixCommandProperties.Setter().withCircuitBreakerSleepWindowInMilliseconds(url.getMethodParameter(method, "sleepWindowInMilliseconds", 5000))
                .withCircuitBreakerErrorThresholdPercentage(url.getMethodParameter(method, "errorThresholdPercentage", 50))
                .withCircuitBreakerRequestVolumeThreshold(url.getMethodParameter(method, "requestVolumeThreshold", 20))
                .withExecutionIsolationThreadInterruptOnTimeout(true)
                .withExecutionTimeoutInMilliseconds(url.getMethodParameter(method, "timeoutInMilliseconds", 1000))
                .withFallbackIsolationSemaphoreMaxConcurrentRequests(url.getMethodParameter(method, "fallbackMaxConcurrentRequests", 50))
                .withExecutionIsolationStrategy(IsolationStrategy.getIsolationStrategy(url))
                .withExecutionIsolationSemaphoreMaxConcurrentRequests(url.getMethodParameter(method, "maxConcurrentRequests", 10));

    }
}
