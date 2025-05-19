package com.example.weatherdemo.service.impl.wether.factory.weatherdatanormalezfactory;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.weatherdemo.service.impl.wether.factory.weatherdatanormalezfactory.beans.WeatherDataNormalizer;
import com.example.weatherdemo.service.impl.wether.factory.weatherdatanormalezfactory.technicalbeans.WeatherDataNormalizerType;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WeatherDataFactory {

    private final Map<WeatherDataNormalizerType, WeatherDataNormalizer> normalizersStrategyMap;

    public WeatherDataNormalizer createWeatherData(Map<String, Object> rawData) {
        WeatherDataNormalizerType type;
        if (rawData.containsKey("temp")) {
            type = WeatherDataNormalizerType.YANDEX;
        } else if (rawData.containsKey("temperature")) {
            type = WeatherDataNormalizerType.GISMETEO;
        } else if (rawData.containsKey("weather")) {
            type = WeatherDataNormalizerType.METEOINFO;
        } else {
            throw new IllegalArgumentException("Unknown weather data format");
        }
        return normalizersStrategyMap.get(type);
    }
}
