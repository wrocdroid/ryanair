package com.home.dev.ifs.service.schedule;

import com.home.dev.ifs.config.AppConfig;
import com.home.dev.ifs.model.Schedule;
import com.home.dev.ifs.model.ScheduleRequestDto;
import com.home.dev.ifs.util.UrlBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ScheduleConsumerImpl implements ScheduleConsumer {

    @Autowired
    private AppConfig config;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UrlBuilder urlBuilder;

    @Override
    public List<Schedule> getSchedulesForGivenMonth(ScheduleRequestDto scheduleRequest) {
        ResponseEntity<List<Schedule>> response = restTemplate.exchange(
                getParametrisedUrl(scheduleRequest),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Schedule>>() {
                });
        return response.getBody();
    }

    private String getParametrisedUrl(ScheduleRequestDto scheduleRequest) {
        Map<String, String> params = new HashMap<>();
        params.put("departure", scheduleRequest.getDeparture());
        params.put("arrival", scheduleRequest.getArrival());
        params.put("year", String.valueOf(scheduleRequest.getYear()));
        params.put("month", String.valueOf(scheduleRequest.getMonth()));

        return urlBuilder.constructUrlWithParams(config.getSchedulesUrl(), params);
    }

}
