package com.example.weatherdemo.service.impl.wether.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.springframework.stereotype.Component;

import com.example.weatherdemo.model.WeatherData;

@Component
public class WeatherUtils {
    public double calculateAverageTemperature(List<WeatherData> weatherDataList) {
        return BigDecimal.valueOf(weatherDataList.stream()
                        .mapToDouble(WeatherData::getTemperature)
                        .average()
                        .orElse(0.0))
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }

    public double calculateAverageHumidity(List<WeatherData> weatherDataList) {
        return BigDecimal.valueOf(weatherDataList.stream()
                        .mapToDouble(WeatherData::getHumidity)
                        .average()
                        .orElse(0.0))
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }
}
