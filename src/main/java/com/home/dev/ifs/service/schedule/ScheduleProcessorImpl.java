package com.home.dev.ifs.service.schedule;

import com.home.dev.ifs.model.Flight;
import com.home.dev.ifs.model.FlightDay;
import com.home.dev.ifs.model.FlightLeg;
import com.home.dev.ifs.model.Schedule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ScheduleProcessorImpl implements ScheduleProcessor {

    @Autowired
    private ScheduleFilterService scheduleFilterService;

    @Override
    public List<FlightLeg> processSchedules(List<Schedule> schedules, FlightLeg flightInfoRequest) {
        final LocalDateTime requestDepartureDateTime = flightInfoRequest.getDepartureDateTime();
        final LocalDateTime requestArrivalDateTime = flightInfoRequest.getArrivalDateTime();

        final List<FlightLeg> dailyFilteredFlightLegs = new ArrayList<>();
        final List<Schedule> extractedSchedules = scheduleFilterService.getSchedulesForRequestedDays(schedules, requestDepartureDateTime, requestArrivalDateTime);

        extractedSchedules.forEach(schedule -> dailyFilteredFlightLegs.addAll(mapToFlightLegs(schedule, flightInfoRequest)));

        return scheduleFilterService.getFlightLegsForRequestedHours(dailyFilteredFlightLegs, flightInfoRequest);
    }

    private List<FlightLeg> mapToFlightLegs(Schedule schedule, FlightLeg flightInfoRequest) {
        return getFlightsPerDay(schedule).entrySet().stream()
                .flatMap(entry -> entry.getValue().stream()
                        .map(flight -> FlightLeg.builder()
                                .departureAirport(flightInfoRequest.getDepartureAirport())
                                .arrivalAirport(flightInfoRequest.getArrivalAirport())
                                .departureDateTime(getFlightDateTime(schedule, entry, flightInfoRequest.getDepartureDateTime(),
                                        flight.getDepartureTime()))
                                .arrivalDateTime(getFlightDateTime(schedule, entry, flightInfoRequest.getArrivalDateTime(),
                                        flight.getArrivalTime()))
                                .build()
                        ))
                .collect(Collectors.toList());
    }

    private Map<Integer, List<Flight>> getFlightsPerDay(Schedule schedule) {
        return schedule.getDays().stream()
                .collect(Collectors.toMap(FlightDay::getDay,
                        flightDay -> new ArrayList<>(flightDay.getFlights())));
    }

    private LocalDateTime getFlightDateTime(Schedule schedule, Map.Entry<Integer, List<Flight>> entry, LocalDateTime departureDateTime, String departureTime) {
        return LocalDateTime.of(
                LocalDate.of(
                        departureDateTime.getYear(),
                        schedule.getMonth(),
                        entry.getKey()),
                LocalTime.parse(departureTime));
    }
}
