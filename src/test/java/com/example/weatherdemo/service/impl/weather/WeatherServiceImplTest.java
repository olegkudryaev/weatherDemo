package com.example.weatherdemo.service.impl.weather;

import com.example.weatherdemo.config.properties.WeatherProperties;
import com.example.weatherdemo.entity.NormalizedWeatherDataEntity;
import com.example.weatherdemo.entity.RawWeatherDataEntity;
import com.example.weatherdemo.model.WeatherData;
import com.example.weatherdemo.repository.NormalizedWeatherDataRepository;
import com.example.weatherdemo.repository.RawWeatherDataRepository;
import com.example.weatherdemo.service.impl.wether.WeatherServiceImpl;
import com.example.weatherdemo.service.impl.wether.factory.weatherdatanormalezfactory.WeatherDataFactory;
import com.example.weatherdemo.service.impl.wether.factory.weatherdatanormalezfactory.beans.WeatherDataNormalizer;
import com.example.weatherdemo.service.impl.wether.utils.WeatherUtils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestBodyUriSpec;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersUriSpec;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WeatherServiceImplTest {

    @Mock
    private RawWeatherDataRepository rawWeatherDataRepository;
    @Mock
    private NormalizedWeatherDataRepository normalizedWeatherDataRepository;
    @Mock
    private WeatherDataFactory weatherDataFactory;
    @Mock
    private WeatherUtils weatherUtils;
    @Mock
    private WeatherProperties weatherProperties;
    @Mock
    private WeatherDataNormalizer weatherDataNormalizer;
    @Mock
    private WebClient webClient;
    @Mock
    private RequestHeadersUriSpec requestHeadersUriSpec;
    @Mock
    private RequestBodyUriSpec requestBodyUriSpec;
    @Mock
    private ResponseSpec responseSpec;

    private WeatherServiceImpl weatherService;

    @BeforeEach
    void setUp() {
        WeatherProperties.WeatherSource weatherSource = new WeatherProperties.WeatherSource();
        weatherSource.setAggregatorUrl("http://localhost:8070");
        weatherSource.setSourceCount(2);
        when(weatherProperties.getWeatherSource()).thenReturn(weatherSource);

        // Setup WebClient mock chain
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString(), any(Long.class))).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.retrieve()).thenReturn(responseSpec);

        weatherService = new WeatherServiceImpl(
                rawWeatherDataRepository,
                normalizedWeatherDataRepository,
                weatherDataFactory,
                weatherUtils,
                weatherProperties
        );
    }

    @Test
    void aggregateWeatherData_ShouldCalculateAverage() {
        // Prepare test data
        Map<String, Object> mockResponse1 = new HashMap<>();
        mockResponse1.put("temp", 20.0);
        mockResponse1.put("hum", 50.0);

        Map<String, Object> mockResponse2 = new HashMap<>();
        mockResponse2.put("temp", 30.0);
        mockResponse2.put("hum", 70.0);

        WeatherData data1 = new WeatherData(20.0, 50.0);
        WeatherData data2 = new WeatherData(30.0, 70.0);
        WeatherData expectedAverage = new WeatherData(25.0, 60.0);

        // Setup WebClient response
        when(responseSpec.bodyToMono(Map.class))
                .thenReturn(Mono.just(mockResponse1))
                .thenReturn(Mono.just(mockResponse2));

        when(weatherDataFactory.createWeatherData(any())).thenReturn(weatherDataNormalizer);
        when(weatherDataNormalizer.normalize(any())).thenReturn(data1, data2);
        when(weatherUtils.calculateAverageTemperature(any())).thenReturn(25.0);
        when(weatherUtils.calculateAverageHumidity(any())).thenReturn(60.0);

        try (MockedStatic<WebClient> webClientMockedStatic = mockStatic(WebClient.class)) {
            webClientMockedStatic.when(() -> WebClient.create(anyString())).thenReturn(webClient);

            StepVerifier.create(weatherService.aggregateWeatherData())
                    .expectNextMatches(actual ->
                            actual.getAverageTemperature() == expectedAverage.getTemperature() &&
                                    actual.getAverageHumidity() == expectedAverage.getHumidity())
                    .verifyComplete();

            verify(rawWeatherDataRepository, times(2)).save(any(RawWeatherDataEntity.class));
            verify(normalizedWeatherDataRepository, times(2)).save(any(NormalizedWeatherDataEntity.class));
        }
    }

    @Test
    void aggregateWeatherData_WhenError_ShouldPropagateError() {
        when(responseSpec.bodyToMono(Map.class))
                .thenReturn(Mono.error(new RuntimeException("Test error")));

        try (MockedStatic<WebClient> webClientMockedStatic = mockStatic(WebClient.class)) {
            webClientMockedStatic.when(() -> WebClient.create(anyString())).thenReturn(webClient);

            StepVerifier.create(weatherService.aggregateWeatherData())
                    .expectError(RuntimeException.class)
                    .verify();
        }
    }
} 