package com.home.dev.ifs.service.schedule;

import com.home.dev.ifs.model.FlightLeg;
import com.home.dev.ifs.model.Schedule;

import java.time.LocalDateTime;
import java.util.List;

public interface ScheduleFilterService {
    List<Schedule> getSchedulesForRequestedDays(List<Schedule> schedules, LocalDateTime requestDepartureDateTime, LocalDateTime requestArrivalDateTime);

    List<FlightLeg> getFlightLegsForRequestedHours(List<FlightLeg> dailyFilteredFlightLegs, FlightLeg flightInfoRequest);
}
