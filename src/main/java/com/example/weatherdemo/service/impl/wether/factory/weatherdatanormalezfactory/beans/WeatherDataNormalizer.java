package com.example.weatherdemo.service.impl.wether.factory.weatherdatanormalezfactory.beans;

import com.example.weatherdemo.model.WeatherData;
import com.example.weatherdemo.service.impl.wether.factory.weatherdatanormalezfactory.technicalbeans.WeatherDataNormalizerType;

import java.util.Map;

public interface WeatherDataNormalizer {
    WeatherData normalize(Map<String, Object> rawData);
    WeatherDataNormalizerType getWeatherDataNormalizerType();
} 