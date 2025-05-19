package com.example.weatherdemo.config.urls;

public interface WeatherUrls {
    String API_INFO = "/";

    interface WeatherAggregator {
        String PART = "aggregator";
        String FULL = API_INFO + PART;
    }

    interface WeatherSource {
        String PART = "source/{id}";
        String FULL = API_INFO + PART;
    }
}
