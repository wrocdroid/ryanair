package com.home.dev.ifs.service.route;

import com.home.dev.ifs.model.Route;

import java.util.List;

public interface RouteService {
    List<Route> getAllRoutes();

    List<Route> getDirectRoutes(String departureAirport, String arrivalAirport, List<Route> allRoutes);

    List<String> getConnectingAirports(String departureAirport, String arrivalAirport, List<Route> allRoutes);
}
