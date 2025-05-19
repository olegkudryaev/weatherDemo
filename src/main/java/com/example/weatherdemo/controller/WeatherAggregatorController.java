package com.example.weatherdemo.controller;

import com.example.weatherdemo.model.WeatherDataOutput;
import com.example.weatherdemo.service.api.weather.WeatherService;
import com.example.weatherdemo.config.urls.WeatherUrls;

import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(WeatherUrls.API_INFO)
public class WeatherAggregatorController {
    private final WeatherService weatherService;

    public WeatherAggregatorController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping(WeatherUrls.WeatherAggregator.FULL)
    public Mono<WeatherDataOutput> getAggregatedWeather() {
        return weatherService.aggregateWeatherData();
    }
} 