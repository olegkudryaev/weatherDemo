package com.example.weatherdemo.service.impl.wether.factory.weatherdatanormalezfactory.technicalbeans;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum WeatherDataNormalizerType {
    YANDEX("temp"),
    GISMETEO("temperature"),
    METEOINFO("weather");

    private final String key;

    private static final Map<String, WeatherDataNormalizerType> weatherDataNormalizerTypeMap = new HashMap<>();

    static {
        for (WeatherDataNormalizerType type : WeatherDataNormalizerType.values()) {
            weatherDataNormalizerTypeMap.put(type.key, type);
        }
    }

    public static WeatherDataNormalizerType getWeatherDataNormalizerType(String key) {
        return Optional.ofNullable(weatherDataNormalizerTypeMap.get(key))
            .orElseThrow(() -> new IllegalArgumentException("Invalid weather data normalizer type: " + key));
    }
}
