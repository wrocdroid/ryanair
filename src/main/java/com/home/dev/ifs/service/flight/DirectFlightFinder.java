package com.home.dev.ifs.service.flight;

import com.home.dev.ifs.model.FlightInfo;
import com.home.dev.ifs.model.FlightLeg;
import com.home.dev.ifs.model.Route;
import com.home.dev.ifs.service.route.RouteService;
import com.home.dev.ifs.service.schedule.ScheduleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class DirectFlightFinder implements FlightFinder {

    private static final Integer STOPS = 0;

    @Autowired
    private RouteService routeService;

    @Autowired
    private ScheduleService scheduleService;

    @Override
    public FlightInfo find(FlightLeg flightInfoRequest, List<Route> allRoutes) {
        final String departureAirport = flightInfoRequest.getDepartureAirport();
        final String arrivalAirport = flightInfoRequest.getArrivalAirport();
        final List<Route> routes = routeService.getDirectRoutes(departureAirport, arrivalAirport, allRoutes);
        if (routes.size() > 0) {
            return new FlightInfo(STOPS, scheduleService.fetchFlightLegs(flightInfoRequest));
        } else {
            log.info("No direct flights found between : {} and {}", departureAirport, arrivalAirport);
            return new FlightInfo(STOPS, Collections.emptyList());
        }
    }

}
