package com.home.dev.ifs;

import com.home.dev.ifs.model.Route;
import com.home.dev.ifs.model.ScheduleRequestDto;
import com.home.dev.ifs.service.route.RouteConsumer;
import com.home.dev.ifs.service.schedule.ScheduleConsumer;
import com.home.dev.ifs.util.TestDataUtil;
import org.hamcrest.Matchers;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class InterconnectingFlightsServiceApplicationTests {

    private static List<Route> routes;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RouteConsumer routeConsumer;

    @MockBean
    private ScheduleConsumer scheduleConsumer;

    @BeforeClass
    public static void testDataSetUp() {
        routes = TestDataUtil.initDefaultTestRoutes();
    }

    @Test
    public void shouldReturnEmptyFlightInfoList() throws Exception {
        when(routeConsumer.getAllRoutes()).thenReturn(routes);
        when(scheduleConsumer.getSchedulesForGivenMonth(any(ScheduleRequestDto.class))).thenReturn(TestDataUtil.initDefaultTestSchedules());

        this.mockMvc.perform(
                buildRequest(TestDataUtil.DUB_AIRPORT, TestDataUtil.WRO_AIRPORT, "2016-03-01T07:00", "2016-03-03T21:00"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(TestDataUtil.DEFAULT_EMPTY_RESPONSE));
    }

    @Test
    public void shouldReturnDirectFlightsOnly() throws Exception {
        LocalDateTime departureTime = LocalDateTime.parse("2016-03-01T00:00");
        LocalDateTime arrivalTime = LocalDateTime.parse("2016-03-03T23:59");
        int flightsPerDay = 1;

        String airportFrom = TestDataUtil.DUB_AIRPORT;
        String airportTo = TestDataUtil.WRO_AIRPORT;
        ScheduleRequestDto scheduleRequestDto = ScheduleRequestDto.builder()
                .departure(airportFrom)
                .arrival(airportTo)
                .year(departureTime.getYear())
                .month(departureTime.getMonth().getValue())
                .build();

        when(routeConsumer.getAllRoutes()).thenReturn(TestDataUtil.generateRoutes(
                TestDataUtil.buildRoute(airportFrom, airportTo)));
        when(scheduleConsumer.getSchedulesForGivenMonth(any(ScheduleRequestDto.class)))
                .thenReturn(TestDataUtil.generateTestSchedules(scheduleRequestDto, departureTime, arrivalTime, flightsPerDay));

        this.mockMvc.perform(buildRequest(airportFrom,
                airportTo,
                "2016-03-01T07:00",
                "2016-03-03T21:00")
                .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("[0]['legs'][0]['departureAirport']", Matchers.containsString(airportFrom)))
                .andExpect(jsonPath("[0]['legs'][0]['arrivalAirport']", Matchers.containsString(airportTo)));
    }

    @Test
    @Ignore
    public void shouldReturnOneInterconnectedFlight() throws Exception {

    }

    @Test
    @Ignore
    public void shouldReturnBothInterconnectedAndDirectFlights() throws Exception {

    }

    @Test
    @Ignore
    public void shouldReturnCrossMonthFlights() throws Exception {
        // case where departure date and arrival date are not in same month
        // (e.g. departureDateTime = 2018-08-31T22:00 and arrivalDateTime = 2018-09-01T01:00)
    }

    private MockHttpServletRequestBuilder buildRequest(String departure, String arrival, String departureDateTime, String arrivalDateTime) {
        return get("/interconnections")
                .param("departure", departure)
                .param("arrival", arrival)
                .param("departureDateTime", departureDateTime)
                .param("arrivalDateTime", arrivalDateTime);
    }

}
