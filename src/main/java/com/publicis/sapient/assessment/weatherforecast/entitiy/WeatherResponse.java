package com.publicis.sapient.assessment.weatherforecast.entitiy;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeatherResponse {
  private String cod;
  private Integer message;
  private Integer cnt;
  private List<list> list;
  private City city;

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class list {
    private Integer dt;
    private Main main;
    private List<Weather> weather;
    private Clouds clouds;
    private Wind wind;
    private Integer visibility;
    private Integer pop;
    private Sys sys;

    @JsonProperty("dt_txt")
    private String dtTxt;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Main {
      private Integer temp;

      @JsonProperty("feels_like")
      private Integer feelsLike;

      @JsonProperty("temp_min")
      private Integer tempMin;

      @JsonProperty("temp_max")
      private Integer tempMax;

      private Integer pressure;

      @JsonProperty("sea_level")
      private Integer seaLevel;

      @JsonProperty("grnd_level")
      private Integer grndLevel;

      private Integer humidity;

      @JsonProperty("temp_kf")
      private Integer tempKf;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Weather {
      private Integer id;
      private String main;
      private String description;
      private String icon;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class Clouds {
      private Integer all;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class Wind {
      private Integer speed;
      private Integer deg;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class Sys {
      private String pod;
    }
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  private static class City {
    private Integer id;
    private String name;
    private Coord coord;
    private String country;
    private Integer population;
    private Integer timezone;
    private Integer sunrise;
    private Integer sunset;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class Coord {
      private Double lat;
      private Double lon;
    }
  }
}
