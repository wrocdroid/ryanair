package com.home.dev.ifs.model;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class FlightLeg {
    private String departureAirport;
    private String arrivalAirport;
    private LocalDateTime departureDateTime;
    private LocalDateTime arrivalDateTime;
}
