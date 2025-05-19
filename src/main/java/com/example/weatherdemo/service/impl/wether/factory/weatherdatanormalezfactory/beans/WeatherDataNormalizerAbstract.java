package com.example.weatherdemo.service.impl.wether.factory.weatherdatanormalezfactory.beans;

import com.example.weatherdemo.model.WeatherData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public abstract class WeatherDataNormalizerAbstract implements WeatherDataNormalizer {
 
    protected double temperature;
    protected double humidity;

    public static WeatherData createWeatherData(double temperature, double humidity) {
        return new WeatherData(temperature, humidity);
    }
}
