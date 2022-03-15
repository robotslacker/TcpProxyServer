package com.robotslacker.tcpproxy.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServerConfig {
    @Value("${server.port}")
    public int port;
}
