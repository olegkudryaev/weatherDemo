package com.example.weatherdemo.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Data
@Configuration
@ConfigurationProperties(prefix = "weather-properties")
public class WeatherProperties {

    private WeatherSource weatherSource;

    @Getter
    @Setter
    public static class WeatherSource {
        private String aggregatorUrl;
        private String sourceUrl;
        private int sourceCount;
    }
}
