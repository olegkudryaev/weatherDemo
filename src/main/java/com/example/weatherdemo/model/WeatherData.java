package com.example.weatherdemo.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WeatherData {
    private double temperature;
    private double humidity;

    public WeatherData(double temperature, double humidity) {
        this.temperature = temperature;
        this.humidity = humidity;
    }
} 