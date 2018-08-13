package com.home.dev.ifs.service.flight;

import com.home.dev.ifs.model.FlightInfo;
import com.home.dev.ifs.model.FlightLeg;
import com.home.dev.ifs.model.Route;
import com.home.dev.ifs.service.route.RouteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class FlightSearchServiceImpl implements FlightSearchService {

    @Autowired
    private DirectFlightFinder directFlightFinder;

    @Autowired
    private RouteService routeService;

    @Autowired
    private InterconnectedFlightFinder interconnectedFlightFinder;

    @Override
    public List<FlightInfo> findFlights(FlightLeg flightInfoRequest) {
        final List<Route> allRoutes = routeService.getAllRoutes();
        final List<FlightInfo> allMatchedFlights = new ArrayList<>();
        if (allRoutes.size() > 0) {
            allMatchedFlights.add(directFlightFinder.find(flightInfoRequest, allRoutes));
            allMatchedFlights.add(interconnectedFlightFinder.find(flightInfoRequest, allRoutes));
        } else {
            log.warn("There are no routes found");
        }
        return allMatchedFlights;
    }
}
