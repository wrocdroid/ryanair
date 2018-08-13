package com.home.dev.ifs.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class FlightInfo {
    private Integer stops;
    private List<FlightLeg> legs;
}
