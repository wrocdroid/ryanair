package com.home.dev.ifs.service.route;

import com.home.dev.ifs.config.AppConfig;
import com.home.dev.ifs.model.Route;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class RouteConsumerImpl implements RouteConsumer {

    @Autowired
    private AppConfig config;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public List<Route> getAllRoutes() {
        final ResponseEntity<List<Route>> response = restTemplate.exchange(
                config.getRoutesUrl(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Route>>() {
                });

        final List<Route> responseBody = response.getBody();
        int routesAmount = responseBody != null ? responseBody.size() : 0;

        if (routesAmount > 0) {
            log.info("Found {} routes in total", routesAmount);
            return responseBody;
        } else {
            return Collections.emptyList();
        }
    }
}
