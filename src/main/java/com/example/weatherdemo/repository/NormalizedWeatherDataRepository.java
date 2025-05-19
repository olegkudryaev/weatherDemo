package com.example.weatherdemo.repository;

import com.example.weatherdemo.entity.NormalizedWeatherDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NormalizedWeatherDataRepository extends JpaRepository<NormalizedWeatherDataEntity, Long> {
} 