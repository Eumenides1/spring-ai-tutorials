package com.rookie.stack.springaitutorials.controller.function;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author eumenides
 * @description
 * @date 2024/6/7
 */
@ConfigurationProperties(value = "weather")
public record WeatherConfigProperties(String apiKey, String apiUrl) {

}
