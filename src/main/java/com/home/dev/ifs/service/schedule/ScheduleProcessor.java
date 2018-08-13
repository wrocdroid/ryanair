package com.home.dev.ifs.service.schedule;

import com.home.dev.ifs.model.FlightLeg;
import com.home.dev.ifs.model.Schedule;

import java.util.List;

public interface ScheduleProcessor {
    List<FlightLeg> processSchedules(List<Schedule> schedules, FlightLeg flightInfoRequest);
}
