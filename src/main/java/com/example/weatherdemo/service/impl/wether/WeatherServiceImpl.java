package com.example.weatherdemo.service.impl.wether;

import com.example.weatherdemo.entity.RawWeatherDataEntity;
import com.example.weatherdemo.config.properties.WeatherProperties;
import com.example.weatherdemo.config.urls.WeatherUrls;
import com.example.weatherdemo.entity.NormalizedWeatherDataEntity;
import com.example.weatherdemo.model.WeatherData;
import com.example.weatherdemo.model.WeatherDataOutput;
import com.example.weatherdemo.model.WeatherEntities;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class WeatherServiceImpl implements WeatherService {
    private final RawWeatherDataRepository rawWeatherDataRepository;
    private final NormalizedWeatherDataRepository normalizedWeatherDataRepository;
    private final WeatherDataFactory weatherDataFactory;
    private final WeatherUtils weatherUtils;
    private final WeatherProperties weatherProperties;
    private static final int BATCH_SIZE = 100;

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
                .flatMap(weatherDataList -> {
                    WeatherEntities weatherEntities = convertToEntities(weatherDataList);
                    return saveWeatherDataBatch(weatherEntities.rawEntities(), weatherEntities.normalizedEntities())
                            .thenReturn(calculateAverage(weatherDataList));
                });
    }

    private Mono<WeatherData> fetchWeatherData(Long sourceId) {
        return WebClient.create(weatherProperties.getWeatherSource().getAggregatorUrl())
                .get()
                .uri(WeatherUrls.WeatherSource.FULL, sourceId)
                .retrieve()
                .bodyToMono(Map.class)
                .map(rawData -> {
                    WeatherData normalizedData = normalizeWeatherData(rawData);
                    normalizedData.setSourceId(sourceId);
                    normalizedData.setRawData(rawData);
                    return normalizedData;
                });
    }

    private WeatherData normalizeWeatherData(Map<String, Object> rawData) {
        return weatherDataFactory.createWeatherData(rawData).normalize(rawData);
    }

    private WeatherEntities convertToEntities(List<WeatherData> weatherDataList) {
        List<RawWeatherDataEntity> rawEntities = new ArrayList<>();
        List<NormalizedWeatherDataEntity> normalizedEntities = new ArrayList<>();
        
        for (WeatherData data : weatherDataList) {
            rawEntities.add(new RawWeatherDataEntity(data.getSourceId(), data.getRawData()));
            normalizedEntities.add(new NormalizedWeatherDataEntity(data.getSourceId(), data));
        }
        
        return new WeatherEntities(rawEntities, normalizedEntities);
    }

    @Transactional
    protected Mono<Void> saveWeatherDataBatch(List<RawWeatherDataEntity> rawEntities,
                                            List<NormalizedWeatherDataEntity> normalizedEntities) {
        return Mono.fromRunnable(() -> {
            for (int i = 0; i < rawEntities.size(); i += BATCH_SIZE) {
                int endIndex = Math.min(i + BATCH_SIZE, rawEntities.size());
                List<RawWeatherDataEntity> batch = rawEntities.subList(i, endIndex);
                rawWeatherDataRepository.saveAll(batch);
            }
            for (int i = 0; i < normalizedEntities.size(); i += BATCH_SIZE) {
                int endIndex = Math.min(i + BATCH_SIZE, normalizedEntities.size());
                List<NormalizedWeatherDataEntity> batch = normalizedEntities.subList(i, endIndex);
                normalizedWeatherDataRepository.saveAll(batch);
            }
        });
    }

    private WeatherDataOutput calculateAverage(List<WeatherData> weatherDataList) {
        double avgTemp = weatherUtils.calculateAverageTemperature(weatherDataList);
        double avgHumidity = weatherUtils.calculateAverageHumidity(weatherDataList);

        return new WeatherDataOutput(avgTemp, avgHumidity);
    }
} 