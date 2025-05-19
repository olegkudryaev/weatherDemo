package com.example.weatherdemo.service.impl.wether;

import com.example.weatherdemo.entity.RawWeatherDataEntity;
import com.example.weatherdemo.config.properties.WeatherProperties;
import com.example.weatherdemo.config.urls.WeatherUrls;
import com.example.weatherdemo.entity.NormalizedWeatherDataEntity;
import com.example.weatherdemo.model.WeatherData;
import com.example.weatherdemo.model.WeatherDataOutput;
import com.example.weatherdemo.repository.RawWeatherDataRepository;
import com.example.weatherdemo.repository.NormalizedWeatherDataRepository;
import com.example.weatherdemo.service.api.weather.WeatherService;
import com.example.weatherdemo.service.impl.wether.factory.weatherdatanormalezfactory.WeatherDataFactory;
import com.example.weatherdemo.service.impl.wether.utils.WeatherUtils;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.List;

@Service
public class WeatherServiceImpl implements WeatherService {
    private final RawWeatherDataRepository rawWeatherDataRepository;
    private final NormalizedWeatherDataRepository normalizedWeatherDataRepository;
    private final WeatherDataFactory weatherDataFactory;
    private final WeatherUtils weatherUtils;
    private final WeatherProperties weatherProperties;

    public WeatherServiceImpl(RawWeatherDataRepository rawWeatherDataRepository,
                              NormalizedWeatherDataRepository normalizedWeatherDataRepository,
                              WeatherDataFactory weatherDataFactory,
                              WeatherUtils weatherUtils, 
                              WeatherProperties weatherProperties) {
        this.rawWeatherDataRepository = rawWeatherDataRepository;
        this.normalizedWeatherDataRepository = normalizedWeatherDataRepository;
        this.weatherDataFactory = weatherDataFactory;
        this.weatherUtils = weatherUtils;
        this.weatherProperties = weatherProperties;
    }

    @Transactional
    public Mono<WeatherDataOutput> aggregateWeatherData() {
        return Flux.range(1, weatherProperties.getWeatherSource().getSourceCount())
                .flatMap(id -> fetchWeatherData(id.longValue()))
                .collectList()
                .map(this::calculateAverage);
    }

    private Mono<WeatherData> fetchWeatherData(Long sourceId) {
        return WebClient.create(weatherProperties.getWeatherSource().getAggregatorUrl())
                .get()
                .uri(WeatherUrls.WeatherSource.FULL, sourceId)
                .retrieve()
                .bodyToMono(Map.class)
                .flatMap(rawData -> {
                    saveRawWeather(sourceId, rawData);
                    WeatherData normalizedData = normalizeWeatherData(rawData);
                    saveNormalizeWetherData(sourceId, normalizedData);
                    return Mono.just(normalizedData);
                });
    }

    private void saveRawWeather(Long sourceId, Map<String, Object> rawData) {
        RawWeatherDataEntity rawEntity = new RawWeatherDataEntity(sourceId, rawData);
        rawWeatherDataRepository.save(rawEntity);
    }

    private WeatherData normalizeWeatherData(Map<String, Object> rawData) {
        return weatherDataFactory.createWeatherData(rawData).normalize(rawData);
    }

    private void saveNormalizeWetherData(Long sourceId, WeatherData normalizedData) {
        NormalizedWeatherDataEntity normalizedEntity = new NormalizedWeatherDataEntity(sourceId, normalizedData);
        normalizedWeatherDataRepository.save(normalizedEntity);
    }

    private WeatherDataOutput calculateAverage(List<WeatherData> weatherDataList) {
        double avgTemp = weatherUtils.calculateAverageTemperature(weatherDataList);
        double avgHumidity = weatherUtils.calculateAverageHumidity(weatherDataList);

        return new WeatherDataOutput(avgTemp, avgHumidity);
    }
} 