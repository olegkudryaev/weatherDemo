package com.example.weatherdemo.model;

import java.util.List;

import com.example.weatherdemo.entity.NormalizedWeatherDataEntity;
import com.example.weatherdemo.entity.RawWeatherDataEntity;

public record WeatherEntities(
    List<RawWeatherDataEntity> rawEntities,
    List<NormalizedWeatherDataEntity> normalizedEntities) {}
