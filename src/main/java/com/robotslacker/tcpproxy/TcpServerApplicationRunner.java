package com.robotslacker.tcpproxy;

import com.robotslacker.tcpproxy.config.TcpProxyServerConfig;
import com.robotslacker.tcpproxy.service.TcpProxyBaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class TcpServerApplicationRunner implements ApplicationRunner {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private TcpProxyServerConfig tcpProxyServerConfig;

    @Override
    public void run(ApplicationArguments args) {
        logger.info("系统启动中, ...");
        logger.info("程序编译时间： " + tcpProxyServerConfig.appBuildTime);
        logger.info("系统已经启动就绪，等待连接中...");
        TcpProxyBaseService.serviceStatus = "RUNNING";
    }
}
