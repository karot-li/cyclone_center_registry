package com.cyclone.integration.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("cyclone.client.register.info")
public class RegisterConfigProperties {

    @Value("${cyclone.client.service-url.defaultZone}")
    private String defaultZone;

    private String hostname;

    private long eliminationTime;

    public String getDefaultZone() {
        return defaultZone;
    }

    public void setDefaultZone(String defaultZone) {
        this.defaultZone = defaultZone;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public long getEliminationTime() {
        return eliminationTime;
    }

    public void setEliminationTime(long eliminationTime) {
        this.eliminationTime = eliminationTime;
    }
}
