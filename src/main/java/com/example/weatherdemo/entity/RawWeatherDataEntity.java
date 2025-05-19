package com.example.weatherdemo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Table(name = "raw_weather_data")
@Getter
@Setter
public class RawWeatherDataEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "source_id")
    private Long sourceId;
    
    @Column(columnDefinition = "TEXT")
    private String payload;
    
    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    public RawWeatherDataEntity(Long sourceId, Map<String, Object> rawData) {
        this.setSourceId(sourceId);
        this.setPayload(rawData.toString());
        this.setTimestamp(LocalDateTime.now());
    }

    public RawWeatherDataEntity() {

    }
}