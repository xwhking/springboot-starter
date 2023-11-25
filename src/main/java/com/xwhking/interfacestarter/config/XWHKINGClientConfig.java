package com.xwhking.interfacestarter.config;

import com.xwhking.interfacestarter.client.XWHKINGClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
@Configuration
@ConfigurationProperties(prefix = "xwhking.client")
@Data
@ComponentScan
public class XWHKINGClientConfig {
    private String accessKey;
    private String secretKey;
    private String userId;
    @Bean
    public XWHKINGClient xwhkingClient(){
        return new XWHKINGClient(accessKey,secretKey,Long.parseLong(userId));
    }
}
