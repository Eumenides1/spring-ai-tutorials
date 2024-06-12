package com.rookie.stack.springaitutorials;

import com.rookie.stack.springaitutorials.controller.function.WeatherConfigProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(WeatherConfigProperties.class)
@SpringBootApplication
public class SpringAiTutorialsApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringAiTutorialsApplication.class, args);
    }

}
