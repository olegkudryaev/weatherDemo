package com.example.weatherdemo.service.impl.wether.factory.weatherdatanormalezfactory.beans;

import com.example.weatherdemo.model.WeatherData;
import com.example.weatherdemo.service.impl.wether.factory.weatherdatanormalezfactory.technicalbeans.WeatherDataNormalizerType;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
@Service
@AllArgsConstructor
public class MeteoinfoWeatherDataNormalizer extends WeatherDataNormalizerAbstract {
    @Override
    public WeatherData normalize(Map<String, Object> rawData) {
        Map<String, Object> weather = (Map<String, Object>) rawData.get("weather");
        super.temperature = ((Number) weather.get("t")).doubleValue();
        super.humidity = ((Number) weather.get("h")).doubleValue();
        return super.createWeatherData(super.temperature, super.humidity);
    }

	@Override
	public WeatherDataNormalizerType getWeatherDataNormalizerType() {
		return WeatherDataNormalizerType.METEOINFO;
	}
}