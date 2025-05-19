package com.example.weatherdemo.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class WeatherData {
    private double temperature;
    private double humidity;
    private Long sourceId;
    private Map<String, Object> rawData;

    public WeatherData(double temperature, double humidity) {
        this.temperature = temperature;
        this.humidity = humidity;
    }

    public WeatherData() {
    }
} 