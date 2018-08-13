package com.home.dev.ifs.service.route;

import com.home.dev.ifs.model.Route;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Slf4j
@Service
public class RouteServiceImpl implements RouteService {

    @Autowired
    private RouteConsumer routeConsumer;

    @Override
    public List<Route> getAllRoutes() {
        return routeConsumer.getAllRoutes();
    }

    @Override
    public List<Route> getDirectRoutes(String departureAirport, String arrivalAirport, List<Route> allRoutes) {
        return allRoutes.stream()
                .filter(directRoutesPredicate(departureAirport, arrivalAirport))
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getConnectingAirports(String departureAirport, String arrivalAirport, List<Route> allRoutes) {
        final List<String> allRoutesFromDepartureAirport =
                getRoutesNamesForAirport(allRoutes, routesFromAirportPredicate(departureAirport), getAirportToFunction());

        final List<String> allRoutesToArrivalAirport =
                getRoutesNamesForAirport(allRoutes, routesToAirportPredicate(arrivalAirport), getAirportFromFunction());

        allRoutesFromDepartureAirport.retainAll(allRoutesToArrivalAirport);

        return allRoutesFromDepartureAirport;
    }

    private List<String> getRoutesNamesForAirport(List<Route> allRoutes,
                                                  Predicate<Route> routesForAirportPredicate,
                                                  Function<Route, String> getAirportFunction) {
        return allRoutes.stream()
                .filter(routesForAirportPredicate)
                .map(getAirportFunction)
                .collect(Collectors.toList());
    }

    private Function<Route, String> getAirportFromFunction() {
        return Route::getAirportFrom;
    }

    private Function<Route, String> getAirportToFunction() {
        return Route::getAirportTo;
    }

    private Predicate<Route> routesToAirportPredicate(String arrivalAirport) {
        return route -> route.getAirportTo().equalsIgnoreCase(arrivalAirport);
    }

    private Predicate<Route> routesFromAirportPredicate(String departureAirport) {
        return route -> route.getAirportFrom().equalsIgnoreCase(departureAirport);
    }

    private Predicate<Route> directRoutesPredicate(String departureAirport, String arrivalAirport) {
        return routesFromAirportPredicate(departureAirport).and(routesToAirportPredicate(arrivalAirport));
    }
}
