package com.example.weatherdemo.service.api.weather;

import com.example.weatherdemo.model.WeatherDataOutput;
import reactor.core.publisher.Mono;

public interface WeatherService {
    Mono<WeatherDataOutput> aggregateWeatherData();
}
