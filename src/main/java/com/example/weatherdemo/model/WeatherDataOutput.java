package com.example.weatherdemo.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WeatherDataOutput {
    private double averageTemperature;
    private double averageHumidity;

    public WeatherDataOutput(double temperature, double humidity) {
        this.averageTemperature = temperature;
        this.averageHumidity = humidity;
    }
}
