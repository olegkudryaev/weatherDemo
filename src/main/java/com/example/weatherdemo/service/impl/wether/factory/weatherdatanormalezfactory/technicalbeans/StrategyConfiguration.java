package com.example.weatherdemo.service.impl.wether.factory.weatherdatanormalezfactory.technicalbeans;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.weatherdemo.service.impl.wether.factory.weatherdatanormalezfactory.beans.WeatherDataNormalizer;

@Configuration
public class StrategyConfiguration {
    @Bean
    public Map<WeatherDataNormalizerType, WeatherDataNormalizer> normalizersStrategyMap(
        List<WeatherDataNormalizer> normalizers) {
        return normalizers.stream()
            .collect(Collectors.toMap(WeatherDataNormalizer::getWeatherDataNormalizerType, Function.identity()));
    }
}
