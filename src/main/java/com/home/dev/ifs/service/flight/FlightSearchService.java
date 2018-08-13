package com.home.dev.ifs.service.flight;

import com.home.dev.ifs.model.FlightInfo;
import com.home.dev.ifs.model.FlightLeg;

import java.util.List;

public interface FlightSearchService {
    List<FlightInfo> findFlights(FlightLeg flightInfoRequest);
}
