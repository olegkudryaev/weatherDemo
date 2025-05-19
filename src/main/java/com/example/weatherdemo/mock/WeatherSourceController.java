package com.example.weatherdemo.mock;

import org.springframework.web.bind.annotation.*;
import java.util.Random;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/source")
public class WeatherSourceController {
    private final Random random = new Random();

    @GetMapping("/{id}")
    public Map<String, Object> getWeatherData(@PathVariable Long id) {
        long format = id % 3;
        
        switch ((int)format) {
            case 0:
                return Map.of(
                    "temp", 15.0 + random.nextDouble() * 10,
                    "hum", 40 + random.nextInt(40)
                );
            case 1:
                return Map.of(
                    "temperature", String.format("%.1f", 15.0 + random.nextDouble() * 10),
                    "humidity", String.format("%d", 40 + random.nextInt(40))
                );
            default:
                Map<String, Object> weather = new HashMap<>();
                weather.put("t", 15.0 + random.nextDouble() * 10);
                weather.put("h", 40.0 + random.nextDouble() * 40);
                return Map.of("weather", weather);
        }
    }
} 