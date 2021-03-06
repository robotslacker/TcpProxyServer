package com.robotslacker.tcpproxy.controller;

import com.robotslacker.tcpproxy.service.impl.TcpProxyServiceStaticIImpl;
import com.robotslacker.tcpproxy.service.TcpProxy;
import com.robotslacker.tcpproxy.TcpProxyServerApplication;
import com.robotslacker.tcpproxy.model.CreateProxyRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/tcpProxy")
public class TcpProxyServerController {

    /**
     * 系统标准日志
     */
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private static  TcpProxy tcpProxy;

    @Resource
    private ApplicationContext context;

    /**
     * 关闭应用服务器
     */
    @DeleteMapping("/shutdown")
    @Async("asyncTaskExecutor")
    public void shutdown() {
        logger.info("系统即将停止服务,.....");
        tcpProxy.shutdown();
        SpringApplication.exit(context, () -> 0);
        logger.info("当前服务已经停止.");
        System.exit(0);
    }

    @PostMapping("/restart")
    @Async("asyncTaskExecutor")
    public void restart() {
        logger.info("系统即将重新启动,.....");
        tcpProxy.shutdown();
        TcpProxyServerApplication.restart();
        logger.info("当前服务已经启动.");
    }

    @PostMapping("/createProxy")
    @ResponseBody
    public void createProxy(@RequestBody CreateProxyRequest createProxyRequest) {
        TcpProxyServiceStaticIImpl staticTcpProxyConfig =
            new TcpProxyServiceStaticIImpl(createProxyRequest.getLocalPort(),
                createProxyRequest.getProxyTargetEndPoint().get(0).getTargetAddress(),
                createProxyRequest.getProxyTargetEndPoint().get(0).getTargetPort(),
                createProxyRequest.getWorkerCount());
        tcpProxy = new TcpProxy(staticTcpProxyConfig);
        tcpProxy.start();
    }
}
