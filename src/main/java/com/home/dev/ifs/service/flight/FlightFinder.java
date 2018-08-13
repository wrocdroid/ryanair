package com.home.dev.ifs.service.flight;

import com.home.dev.ifs.model.FlightInfo;
import com.home.dev.ifs.model.FlightLeg;
import com.home.dev.ifs.model.Route;

import java.util.List;

public interface FlightFinder {
    FlightInfo find(FlightLeg flightInfoRequest, List<Route> allRoutes);
}
