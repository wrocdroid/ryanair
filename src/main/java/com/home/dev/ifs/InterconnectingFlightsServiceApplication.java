package com.home.dev.ifs;

import com.home.dev.ifs.config.RestTemplateResponseErrorHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class InterconnectingFlightsServiceApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(InterconnectingFlightsServiceApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.errorHandler(new RestTemplateResponseErrorHandler()).build();
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(InterconnectingFlightsServiceApplication.class);
    }
}