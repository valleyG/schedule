package com.yss.schedule.comfig;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.HashMap;
import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "job")
@PropertySource("classpath:jobParam.properties")
public class JobParamConfig {
    private Map<String,String> param = new HashMap<>();

    public Map<String, String>  getParam() {
        return param;
    }

}
