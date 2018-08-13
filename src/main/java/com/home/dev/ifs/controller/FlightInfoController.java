package com.home.dev.ifs.controller;

import com.home.dev.ifs.model.FlightInfo;
import com.home.dev.ifs.model.FlightLeg;
import com.home.dev.ifs.service.flight.FlightSearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
public class FlightInfoController {

    @Autowired
    private FlightSearchService searchService;

    @RequestMapping("/interconnections")
    public ResponseEntity getFlights(
            @RequestParam(value = "departure") String departure,
            @RequestParam(value = "arrival") String arrival,
            @RequestParam(value = "departureDateTime")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime departureDateTime,
            @RequestParam(value = "arrivalDateTime")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime arrivalDateTime) {

        log.info("Fetching data for {} - {} : {} - {}", departure, arrival, departureDateTime, arrivalDateTime);
        final List<FlightInfo> flightInfoList = searchService.findFlights(FlightLeg.builder()
                .departureAirport(departure)
                .arrivalAirport(arrival)
                .departureDateTime(departureDateTime)
                .arrivalDateTime(arrivalDateTime)
                .build());

        return ResponseEntity.ok().body(flightInfoList);
    }

}
