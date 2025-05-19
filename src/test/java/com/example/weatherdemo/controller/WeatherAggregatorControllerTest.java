package com.example.weatherdemo.controller;

import com.example.weatherdemo.model.WeatherData;
import com.example.weatherdemo.model.WeatherDataOutput;
import com.example.weatherdemo.service.api.weather.WeatherService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.when;

@WebFluxTest(WeatherAggregatorController.class)
class WeatherAggregatorControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private WeatherService weatherService;

    @Test
    void getAggregatedWeather_ShouldReturnWeatherData() {
        WeatherDataOutput expectedData = new WeatherDataOutput(25.5, 60.0);
        when(weatherService.aggregateWeatherData()).thenReturn(Mono.just(expectedData));

        webTestClient.get()
                .uri("/aggregator")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.averageTemperature").isEqualTo(25.5)
                .jsonPath("$.averageHumidity").isEqualTo(60.0);
    }
} 