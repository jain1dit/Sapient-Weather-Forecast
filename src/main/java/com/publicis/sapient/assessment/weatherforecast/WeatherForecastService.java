package com.publicis.sapient.assessment.weatherforecast;

import com.publicis.sapient.assessment.weatherforecast.entitiy.PredictionResponse;
import com.publicis.sapient.assessment.weatherforecast.entitiy.WeatherResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Optional;

@Service
public class WeatherForecastService {

  private static final Logger logger = LoggerFactory.getLogger(WeatherForecastService.class);

  @Value("${weather.api.appid}")
  private String appId;

  public PredictionResponse checkWeatherConditions(String cityName) {
    WeatherResponse weatherResponse = getWeatherForecastByCity(cityName);
    PredictionResponse response = new PredictionResponse();
    Double tempMax =
        weatherResponse.getList().stream()
            .skip(4)
            .limit(24)
            .mapToDouble(a -> a.getMain().getTempMax())
            .average()
            .getAsDouble();
    if (tempMax > 40 + 273.15) {
      logger.debug("Use sunscreen lotion, as Maximum temp is {}", tempMax);
      response.setDecision("Use sunscreen lotion");
      return response;
    }
    Optional<Boolean> takeUmbrella =
        weatherResponse.getList().stream()
            .skip(4)
            .limit(24)
            .map(a -> a.getWeather().stream().anyMatch(c -> c.getDescription().contains("rains")))
            .findAny();

    if (takeUmbrella.isPresent() && takeUmbrella.get()) {
      logger.debug("Bad Weather, Please Carry Umbrella");
      response.setDecision("Take Umbrella");
      return response;
    }
    logger.debug("Its will be Sunny day!");
    response.setDecision("Happy Weather");
    return response;
  }

  @Retryable(
      maxAttemptsExpression = "#{${retry.max.attempts:3}}",
      value = {RuntimeException.class},
      backoff =
          @Backoff(
              delayExpression = "#{${retry.delay:2000}}",
              multiplierExpression = "#{${retry.multiplier:2}}"))
  public WeatherResponse getWeatherForecastByCity(String cityName) {
    logger.info("Calling weather forecast API");
    return new RestTemplate()
        .getForEntity(
            "https://api.openweathermap.org/data/2.5/forecast?q=" + cityName + "&appid=" + appId,
            WeatherResponse.class)
        .getBody();
  }

  @Recover
  public ResponseEntity<String> recover(final RuntimeException e) {
    logger.debug(
        "Sending the fall-back method response to the user as the number of max-attempts for the 3rd-party service has been reached. Exception message {}",
        e.getMessage());
    return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
  }
}
