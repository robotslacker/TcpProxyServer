package com.robotslacker.tcpproxy;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TcpProxyServerApplication {
    public static ConfigurableApplicationContext context;
    public static void main(String[] args) {
        context = SpringApplication.run(TcpProxyServerApplication.class, args);
    }

    public static void restart()
    {
        ApplicationArguments applicationArguments =
                context.getBean(ApplicationArguments.class);
        Thread thread = new Thread(()->{
            context.close();
            context = SpringApplication.run(TcpProxyServerApplication.class, applicationArguments.getSourceArgs());
        });
        thread.setDaemon(false);
        thread.start();
    }

    @Bean(value = "meterRegistryCustomizer")
    MeterRegistryCustomizer<MeterRegistry> meterRegistryCustomizer() {
        return meterRegistry -> meterRegistry.config()
                .commonTags("application", "tcpProxy");
    }
}
