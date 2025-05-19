package com.example.weatherdemo.service.impl.wether.factory.weatherdatanormalezfactory.beans;

import java.util.Map;

import com.example.weatherdemo.model.WeatherData;
import com.example.weatherdemo.service.impl.wether.factory.weatherdatanormalezfactory.technicalbeans.WeatherDataNormalizerType;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class YandexWeatherDataNormalizer extends WeatherDataNormalizerAbstract {

	@Override
	public WeatherData normalize(Map<String, Object> rawData) {
		super.temperature = ((Number) rawData.get("temp")).doubleValue();
		super.humidity = ((Number) rawData.get("hum")).doubleValue();
		return super.createWeatherData(super.temperature, super.humidity);
	}

	@Override
	public WeatherDataNormalizerType getWeatherDataNormalizerType() {
		return WeatherDataNormalizerType.YANDEX;
	}
}
