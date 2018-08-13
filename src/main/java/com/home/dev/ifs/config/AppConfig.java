package com.home.dev.ifs.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:application.properties")
@Getter
public class AppConfig {

    @Value("${ifs.interconnected.flights.stops}")
    private int interconnectionStops;

    @Value("${ifs.interconnected.flights.time.difference}")
    private int mixTimeBetweenFlights;

    @Value("${ifs.routes.api.url:}")
    private String routesUrl;

    @Value("${ifs.schedules.api.url}")
    private String schedulesUrl;

}
