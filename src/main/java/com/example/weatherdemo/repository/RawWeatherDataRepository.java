package com.example.weatherdemo.repository;

import com.example.weatherdemo.entity.RawWeatherDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RawWeatherDataRepository extends JpaRepository<RawWeatherDataEntity, Long> {
} 