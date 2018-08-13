package com.home.dev.ifs.service.route;

import com.home.dev.ifs.model.Route;
import com.home.dev.ifs.util.TestDataUtil;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static com.home.dev.ifs.util.TestDataUtil.*;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RouteServiceImplTest {

    private static List<Route> testRoutes;

    @Mock
    private RouteConsumer routeConsumer;

    @InjectMocks
    private RouteService routeService = new RouteServiceImpl();

    @BeforeClass
    public static void testDataSetUp() {
        testRoutes = TestDataUtil.initDefaultTestRoutes();
    }

    @Test
    public void shouldReturnAllRoutes() {
        when(routeConsumer.getAllRoutes()).thenReturn(testRoutes);

        assertThat(routeService.getAllRoutes(), hasSize(testRoutes.size()));
    }

    @Test
    public void shouldReturnDirectRouteForRequestedDirection() {
        List<Route> directRoutes = routeService.getDirectRoutes(WRO_AIRPORT, CHA_AIRPORT, testRoutes);

        assertThat(directRoutes, hasSize(1));
        assertThat(directRoutes, contains(buildRoute(WRO_AIRPORT, CHA_AIRPORT)));
    }

    @Test
    public void shouldReturnConnectingAirportsForIndirectRoute() {
        List<String> connectingAirports = routeService.getConnectingAirports(WRO_AIRPORT, DUB_AIRPORT, testRoutes);

        assertThat(connectingAirports, hasSize(1));
        assertThat(connectingAirports, contains(STN_AIRPORT));
    }


}