package com.netease.hystrix.dubbo.rpc.filter;

import com.alibaba.dubbo.common.extension.ExtensionLoader;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcResult;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.exception.HystrixRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by yangshaokai on 2018/2/7.
 */
public class DubboCommand extends HystrixCommand<Result> {

    private static Logger logger = LoggerFactory.getLogger(DubboCommand.class);

    private Invoker<?> invoker;
    private Invocation invocation;
    private String fallbackName;

    public DubboCommand(Setter setter, Invoker<?> invoker, Invocation invocation, String fallbackName) {
        super(setter);
        this.invoker = invoker;
        this.invocation = invocation;
        this.fallbackName = fallbackName;
    }

    protected Result run() throws Exception {
        Result result = invoker.invoke(invocation);
        //如果远程调用异常，抛出异常执行降级逻辑
        if (result.hasException()) {
            throw new HystrixRuntimeException(HystrixRuntimeException.FailureType.COMMAND_EXCEPTION, DubboCommand.class, result.getException().getMessage(), result.getException(), null);
        }

        return result;
    }

    @Override
    protected Result getFallback() {

        if (StringUtils.isEmpty(fallbackName)) {
            //抛出原本的异常
            return super.getFallback();
        }
        try {
            //基于SPI扩展加载fallback实现
            ExtensionLoader<Fallback> loader = ExtensionLoader.getExtensionLoader(Fallback.class);
            Fallback fallback = loader.getExtension(fallbackName);
            Object value = fallback.invoke();
            return new RpcResult(value);
        } catch (RuntimeException ex) {
            logger.error("fallback failed", ex);
            throw ex;
        }

    }

}
