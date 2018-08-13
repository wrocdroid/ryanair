package com.home.dev.ifs.service.schedule;

import com.home.dev.ifs.model.FlightLeg;
import com.home.dev.ifs.model.Schedule;
import com.home.dev.ifs.model.ScheduleRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    @Autowired
    private ScheduleConsumer scheduleConsumer;

    @Autowired
    private ScheduleProcessor scheduleProcessor;

    @Override
    public List<FlightLeg> fetchFlightLegs(FlightLeg flightInfoRequest) {
        final List<Schedule> schedules = new ArrayList<>();
        prepareScheduleRequests(flightInfoRequest).forEach(scheduleRequestDto ->
                schedules.addAll(scheduleConsumer.getSchedulesForGivenMonth(scheduleRequestDto).stream()
                        .filter(schedule -> schedule.getDays() != null)
                        .collect(Collectors.toList())));

        return scheduleProcessor.processSchedules(schedules, flightInfoRequest);
    }

    private List<ScheduleRequestDto> prepareScheduleRequests(FlightLeg flightInfoRequest) {
        final List<ScheduleRequestDto> requests = new ArrayList<>();

        final LocalDateTime departureDateTime = flightInfoRequest.getDepartureDateTime();
        final LocalDateTime arrivalDateTime = flightInfoRequest.getArrivalDateTime();

        final String departureAirport = flightInfoRequest.getDepartureAirport();
        final String arrivalAirport = flightInfoRequest.getArrivalAirport();

        if (isEndYearFlight(departureDateTime, arrivalDateTime) || isEndMonthFlight(departureDateTime, arrivalDateTime)) {
            requests.add(createScheduleRequestDto(departureAirport, arrivalAirport, departureDateTime));
            requests.add(createScheduleRequestDto(departureAirport, arrivalAirport, arrivalDateTime));
        } else {
            requests.add(createScheduleRequestDto(departureAirport, arrivalAirport, departureDateTime));
        }

        return requests;
    }

    private ScheduleRequestDto createScheduleRequestDto(String departureAirport, String arrivalAirport, LocalDateTime dateTime) {
        return ScheduleRequestDto.builder()
                .departure(departureAirport)
                .arrival(arrivalAirport)
                .year(dateTime.getYear())
                .month(dateTime.getMonth().getValue())
                .build();
    }

    private boolean isEndMonthFlight(LocalDateTime departureDateTime, LocalDateTime arrivalDateTime) {
        return arrivalDateTime.getMonth().getValue() > departureDateTime.getMonth().getValue();
    }

    private boolean isEndYearFlight(LocalDateTime departureDateTime, LocalDateTime arrivalDateTime) {
        return arrivalDateTime.getYear() > departureDateTime.getYear();
    }
}
