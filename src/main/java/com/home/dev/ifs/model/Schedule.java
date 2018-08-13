package com.home.dev.ifs.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class Schedule {
    private Integer month;
    private List<FlightDay> days;
}
