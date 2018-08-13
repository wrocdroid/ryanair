package com.home.dev.ifs.service.flight;

import com.home.dev.ifs.config.AppConfig;
import com.home.dev.ifs.model.FlightInfo;
import com.home.dev.ifs.model.FlightLeg;
import com.home.dev.ifs.model.Route;
import com.home.dev.ifs.service.route.RouteService;
import com.home.dev.ifs.service.schedule.ScheduleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class InterconnectedFlightFinder implements FlightFinder {

    @Autowired
    private AppConfig config;

    @Autowired
    private RouteService routeService;

    @Autowired
    private ScheduleService scheduleService;

    @Override
    public FlightInfo find(FlightLeg flightInfoRequest, List<Route> allRoutes) {
        final String departureAirport = flightInfoRequest.getDepartureAirport();
        final LocalDateTime departureDateTime = flightInfoRequest.getDepartureDateTime();
        final String arrivalAirport = flightInfoRequest.getArrivalAirport();
        final LocalDateTime arrivalDateTime = flightInfoRequest.getArrivalDateTime();
        final List<String> connectingAirports = routeService.getConnectingAirports(departureAirport, arrivalAirport, allRoutes);

        if (connectingAirports.size() > 0) {
            List<FlightLeg> flightLegs = new ArrayList<>();
            connectingAirports.forEach(airport ->
                    flightLegs.addAll(getFilteredFlightLegs(airport,
                            departureAirport,
                            departureDateTime,
                            arrivalAirport,
                            arrivalDateTime)));

            return new FlightInfo(config.getInterconnectionStops(), flightLegs);
        } else {
            log.info("No interconnected flights found between : {} and {}", departureAirport, arrivalAirport);
            return new FlightInfo(config.getInterconnectionStops(), Collections.emptyList());
        }
    }

    private List<FlightLeg> getFilteredFlightLegs(String interconnectedAirport,
                                                  String departureAirport,
                                                  LocalDateTime departureDateTime,
                                                  String arrivalAirport,
                                                  LocalDateTime arrivalDateTime) {

        final FlightLeg firstFlightInfo =
                createFlightLeg(interconnectedAirport, departureAirport, departureDateTime, arrivalDateTime);
        final FlightLeg secondFlightInfo =
                createFlightLeg(arrivalAirport, interconnectedAirport, departureDateTime, arrivalDateTime);

        final List<FlightLeg> firstFlightList = scheduleService.fetchFlightLegs(firstFlightInfo);
        final List<FlightLeg> secondFlightList = scheduleService.fetchFlightLegs(secondFlightInfo);

        return filterInterconnectedFlights(firstFlightList, secondFlightList);
    }

    private FlightLeg createFlightLeg(String interconnectedAirport,
                                      String departureAirport,
                                      LocalDateTime departureDateTime,
                                      LocalDateTime arrivalDateTime) {
        return FlightLeg.builder()
                .departureAirport(departureAirport)
                .departureDateTime(departureDateTime)
                .arrivalAirport(interconnectedAirport)
                .arrivalDateTime(arrivalDateTime)
                .build();
    }

    private List<FlightLeg> filterInterconnectedFlights(List<FlightLeg> firstFlightList, List<FlightLeg> secondFlightList) {
        final List<FlightLeg> resultList = new ArrayList<>();

        for (FlightLeg firstFlight : firstFlightList) {
            for (FlightLeg secondFlight : secondFlightList) {
                LocalDateTime secondFlightDepartureDateTime = secondFlight.getDepartureDateTime();
                LocalDateTime firstFlightArrivalDateTime = firstFlight.getArrivalDateTime();
                LocalDateTime latestSecondFlightDeparture = firstFlightArrivalDateTime.plusHours(config.getMixTimeBetweenFlights());

                if (secondFlightDepartureDateTime.isAfter(latestSecondFlightDeparture)) {
                    resultList.add(firstFlight);
                    resultList.add(secondFlight);
                }
            }
        }
        return resultList;
    }
}
