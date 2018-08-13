package com.home.dev.ifs.util;

import com.home.dev.ifs.config.JacksonConfiguration;
import com.home.dev.ifs.model.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TestDataUtil {
    public static final String WRO_AIRPORT = "WRO";
    public static final String DUB_AIRPORT = "DUB";
    public static final String CHA_AIRPORT = "CHA";
    public static final String STN_AIRPORT = "STN";

    public static final String DEFAULT_EMPTY_RESPONSE = "[{\"stops\":0,\"legs\":[]},{\"stops\":1,\"legs\":[]}]";

    private static final int defaultMonth = Month.JANUARY.getValue();
    private static final int defaultDaysRange = 1;
    private static final int defaultFlightPerDay = 1;
    private static final String defaultDepartureTime = "00:00";
    private static final String defaultArrivalTime = "23:59";

    public static List<Route> initDefaultTestRoutes() {
        Route directRoute = buildRoute(WRO_AIRPORT, CHA_AIRPORT);
        Route indirectFirstRoute = buildRoute(WRO_AIRPORT, STN_AIRPORT);
        Route directSecondRoute = buildRoute(STN_AIRPORT, DUB_AIRPORT);
        return Arrays.asList(directRoute, indirectFirstRoute, directSecondRoute);
    }

    public static List<Route> generateRoutes(Route... routes) {
        return Arrays.asList(routes);
    }

    public static List<Schedule> initDefaultTestSchedules() {
        return Collections.singletonList(initDefaultTestSchedule());
    }

    public static List<Schedule> generateTestSchedules(ScheduleRequestDto scheduleRequestDto, LocalDateTime departureTime,
                                                       LocalDateTime arrivalTime, int flightsPerDay) {
        return Collections.singletonList(generateSchedule(
                scheduleRequestDto.getMonth(),
                getTimeDifferenceInDays(departureTime, arrivalTime),
                flightsPerDay,
                departureTime.format(JacksonConfiguration.TIME_FORMATTER),
                arrivalTime.format(JacksonConfiguration.TIME_FORMATTER)));
    }

    public static Route buildRoute(String from, String to) {
        return Route.builder().airportFrom(from).airportTo(to).build();
    }

    private static List<FlightDay> generateFlightDayList(int dayRange, int flightsPerDay, String departureTime, String arrivalTime) {
        int firstDayOfMonth = 1;
        return IntStream.range(firstDayOfMonth, dayRange + 1).mapToObj(i -> FlightDay.builder()
                .day(i)
                .flights(generateFlightList(flightsPerDay, departureTime, arrivalTime))
                .build())
                .collect(Collectors.toList());
    }

    private static List<Flight> generateFlightList(int flightsPerDay, String departureTime, String arrivalTime) {
        return IntStream.range(0, flightsPerDay).mapToObj(i -> Flight.builder()
                .number(generateRandomFlightNumber())
                .departureTime(departureTime)
                .arrivalTime(arrivalTime)
                .build())
                .collect(Collectors.toList());
    }

    private static String generateRandomFlightNumber() {
        int leftLimit = 1000;
        int rightLimit = 9999;
        return String.valueOf(leftLimit + (int) (Math.random() * (rightLimit - leftLimit)));
    }

    private static Schedule generateSchedule(int month, int dayRange, int flightsPerDay, String departureTime, String arrivalTime) {
        return Schedule.builder()
                .month(month)
                .days(generateFlightDayList(dayRange, flightsPerDay, departureTime, arrivalTime))
                .build();
    }

    private static Schedule initDefaultTestSchedule() {
        return generateSchedule(defaultMonth, defaultDaysRange, defaultFlightPerDay,
                defaultDepartureTime, defaultArrivalTime);
    }

    private static int getTimeDifferenceInDays(LocalDateTime departure, LocalDateTime arrival) {
        Duration duration = Duration.between(departure, arrival);
        long diff = Math.abs(duration.toDays());
        return Math.toIntExact(diff);
    }


}
