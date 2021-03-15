package com.publicis.sapient.assessment.weatherforecast;

import com.publicis.sapient.assessment.weatherforecast.entitiy.PredictionResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WeatherForecastController {

  private static final Logger logger = LoggerFactory.getLogger(WeatherForecastController.class);

  @Autowired WeatherForecastService service;

  @GetMapping(value = "/weather-forecast/{cityName}")
  public PredictionResponse getWeatherForecast(@PathVariable String cityName) {
    logger.info("Checking weather condition for {} City", cityName);
    return service.checkWeatherConditions(cityName);
  }
}
