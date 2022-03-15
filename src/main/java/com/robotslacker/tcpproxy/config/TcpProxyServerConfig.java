package com.robotslacker.tcpproxy.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
public class TcpProxyServerConfig {
    @Value("${app.build.time}")
    public String appBuildTime;
}
