package com.example.weatherdemo.service.impl.wether.factory.weatherdatanormalezfactory.beans;

import com.example.weatherdemo.model.WeatherData;
import com.example.weatherdemo.service.impl.wether.factory.weatherdatanormalezfactory.technicalbeans.WeatherDataNormalizerType;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@AllArgsConstructor
public class GismeteoWeatherDataNormalizer extends WeatherDataNormalizerAbstract {
    @Override
    public WeatherData normalize(Map<String, Object> rawData) {
        String tempStr = ((String) rawData.get("temperature")).replace(",", ".");
        String humStr = ((String) rawData.get("humidity")).replace(",", ".");
        super.temperature = Double.parseDouble(tempStr);
        super.humidity = Double.parseDouble(humStr);
		return super.createWeatherData(super.temperature, super.humidity);
    }

	@Override
	public WeatherDataNormalizerType getWeatherDataNormalizerType() {
		return WeatherDataNormalizerType.GISMETEO;
	}
}