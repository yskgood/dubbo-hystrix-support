# JAR包引入
通过maven引入编译好的jar包
```
<dependency>
  <groupId>com.netease</groupId>
  <artifactId>dubbo-hystrix-support</artifactId>
  <version>0.0.1</version>
</dependency>
```
引入dubbo-hystrix-support后会通过Dubbo的Activate机制自动激活HystrixFilter,对dubbo:reference进行资源隔离和熔断保护。

# 线程池隔离
默认按dubbo:reference进行线程池资源隔离，可以认为一个dubbo:reference为一个领域服务，reference里的所有方法在一个线程池资源里运行，多个reference之间线程池资源是隔离的，这样可以根据具体的业务场景对不同的reference设置不同的线程池资源，并且当某个reference出现阻塞故障时不会导致容器线程资源被耗尽，从而影响其他服务。

默认线程池配置如下：
```
<dubbo:parameter key="coreSize" value="10"/>
<dubbo:parameter key="maximumSize" value="20"/>
<dubbo:parameter key="keepAliveTimeMinutes" value="1"/>
```
| 参数        | 说明           | 默认值  |
| ------------- |:-------------:| -----:|
| coreSize      | 核心线程数大小 | 10 |
| maximumSize      | 空闲线程持有时间（分钟）      |   20 |
| keepAliveTimeMinutes | 最大线程数大小      |    1 |

# 信号量隔离
将自定义属性 isolation 为设置 SEMAPHORE 进行信号量隔离

` <dubbo:parameter key="isolation" value="SEMAPHORE"/>` 

isolation属性：

| 属性值        | 说明          | 备注 |
| ------------- |:-------------:| :-----|
| SEMAPHORE    | 信号量隔离 | 适用于本地方法调用，或者QPS非常高的调用 |
| THREAD      | 线程池隔离 -默认值      |  |

### 最大并发请求数量
设置属性 maxConcurrentRequests 默认值为 10

`<dubbo:parameter key="maxConcurrentRequests" value="10"/>`


# 熔断保护
每个dubbo:reference的服务方法会封装成command,提供熔断保护和优雅降级功能。

默认熔断配置如下：
```
<dubbo:parameter key="requestVolumeThreshold" value="20"/>
<dubbo:parameter key="sleepWindowInMilliseconds" value="5000"/>
<dubbo:parameter key="errorThresholdPercentage" value="50"/>
<dubbo:parameter key="timeoutInMilliseconds" value="1000"/>
```
| 参数        | 说明           | 默认值  |   备注      |
| ------------- |:-------------| :---- |:---- |
| requestVolumeThreshold      | 熔断判断请求数阈值 | 20 |一个统计周期内（默认10秒）请求不少于requestVolumeThreshold才会进行熔断判断 |
| sleepWindowInMilliseconds     | 熔断触发错误率阈值      |   5000 | 超过50%错误触发熔断|
| errorThresholdPercentage | 熔断触发后多久恢复half-open状态     |    50 |熔断后sleepWindowInMilliseconds毫秒会放入一个请求，如果请求处理成功，熔断器关闭，否则熔断器打开，继续等待sleepWindowInMilliseconds |
| timeoutInMilliseconds | 任务执行超时时间       |    1000 | 注意该时间和dubbo自己的超时时间不要冲突，以这个时间优先，比如consumer设置3秒，那么当执行时hystrix会提前超时 |

# 服务降级
降级触发条件如下：
1. 远程服务调用超时
1. 远程服务内部执行返回异常结果（如内部抛出运行时异常）
1. Hystrix will execute this fallback for all types of failure such as run() failure, timeout, thread pool or semaphore rejection, and circuit-breaker short-circuiting

服务降级方法通过SPI扩展的方式加载、运行，实现方式如下：
### 1、继承实现Fallback接口，提供降级实现，Fallback接口定义如下：
```
package com.netease.urs.dubbo.rpc.filter;
 
import com.alibaba.dubbo.common.extension.SPI;
 
/**
 * 业务失败返回处理函数
 * Created by yangshaokai on 2018/2/26.
 */
@SPI
public interface Fallback {
    Object invoke();
}
```
### 2、按dubbo SPI扩展规则进行配置
1. resources目录增加文件/META-INF/dubbo/com.netease.urs.dubbo.rpc.filter.Fallback
2. demoFallback=com.netease.urs.dubbo.rpc.DemoFallback

### 3、dubbo:reference增加method fallback配置
```
<dubbo:method name="demo">
    <dubbo:parameter key="fallback" value="demoFallback"/>
</dubbo:method>
```
## Fallback并发调用量控制
设置属性 fallbackMaxConcurrentRequests 默认值为 50

`<dubbo:parameter key="fallbackMaxConcurrentRequests" value="50"/>`
