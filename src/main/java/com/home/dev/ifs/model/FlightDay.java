package com.home.dev.ifs.model;

import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class FlightDay {
    private Integer day;
    private List<Flight> flights;
}
