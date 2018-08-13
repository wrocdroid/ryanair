package com.home.dev.ifs.service.schedule;

import com.home.dev.ifs.model.FlightDay;
import com.home.dev.ifs.model.FlightLeg;
import com.home.dev.ifs.model.Schedule;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class ScheduleFilterServiceImpl implements ScheduleFilterService {

    @Override
    public List<Schedule> getSchedulesForRequestedDays(List<Schedule> schedules,
                                                       LocalDateTime requestDepartureDateTime,
                                                       LocalDateTime requestArrivalDateTime) {
        return schedules.stream().map(schedule ->
                Schedule.builder()
                        .month(schedule.getMonth())
                        .days(schedule.getDays().stream()
                                .filter(timeRangeInDaysPredicate(requestDepartureDateTime, requestArrivalDateTime))
                                .collect(Collectors.toList()))
                        .build()).collect(Collectors.toList());
    }

    @Override
    public List<FlightLeg> getFlightLegsForRequestedHours(List<FlightLeg> dailyFilteredFlightLegs, FlightLeg flightInfoRequest) {
        return dailyFilteredFlightLegs.stream()
                .filter(timeRangeInHoursPredicate(flightInfoRequest))
                .collect(Collectors.toList());
    }

    private boolean notLaterThanArrival(FlightLeg flightInfoRequest, FlightLeg dailyFilteredFlightLeg) {
        return dailyFilteredFlightLeg.getArrivalDateTime().isEqual(flightInfoRequest.getArrivalDateTime()) ||
                dailyFilteredFlightLeg.getArrivalDateTime().isBefore(flightInfoRequest.getArrivalDateTime());
    }

    private boolean notEarlierThanDeparture(FlightLeg flightInfoRequest, FlightLeg dailyFilteredFlightLeg) {
        return dailyFilteredFlightLeg.getDepartureDateTime().isEqual(flightInfoRequest.getDepartureDateTime()) ||
                dailyFilteredFlightLeg.getDepartureDateTime().isAfter(flightInfoRequest.getDepartureDateTime());
    }

    private Predicate<FlightLeg> timeRangeInHoursPredicate(FlightLeg flightInfoRequest) {
        return dailyFilteredFlightLeg ->
                notEarlierThanDeparture(flightInfoRequest, dailyFilteredFlightLeg) &&
                        notLaterThanArrival(flightInfoRequest, dailyFilteredFlightLeg);
    }

    private Predicate<FlightDay> timeRangeInDaysPredicate(LocalDateTime requestDepartureDateTime, LocalDateTime requestArrivalDateTime) {
        return flightDay ->
                flightDay.getDay() >= requestDepartureDateTime.getDayOfMonth() &&
                        flightDay.getDay() <= requestArrivalDateTime.getDayOfMonth();
    }
}
