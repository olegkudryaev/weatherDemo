package com.example.weatherdemo.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

import com.example.weatherdemo.model.WeatherData;

@Entity
@Table(name = "normalized_weather_data")
@Data
public class NormalizedWeatherDataEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "source_id")
    private Long sourceId;
    
    @Column(name = "temperature")
    private double temperature;

    @Column(name = "humidity")
    private double humidity;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    public NormalizedWeatherDataEntity(Long sourceId, WeatherData normalizedData) {
        this.sourceId = sourceId;
        this.temperature = normalizedData.getTemperature();
        this.humidity = normalizedData.getHumidity();
        this.timestamp = LocalDateTime.now();
    }

    public NormalizedWeatherDataEntity() {

    }
}