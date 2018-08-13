package com.home.dev.ifs.service.schedule;

import com.home.dev.ifs.model.FlightLeg;

import java.util.List;

public interface ScheduleService {
    List<FlightLeg> fetchFlightLegs(FlightLeg flightInfoRequest);
}
