package com.netease.hystrix.dubbo.rpc.filter;

import com.alibaba.dubbo.common.extension.SPI;

/**
 * 业务失败返回处理函数
 * Created by yangshaokai on 2018/2/26.
 */
@SPI
public interface Fallback {
    Object invoke();
}
