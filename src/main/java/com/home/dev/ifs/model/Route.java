package com.home.dev.ifs.model;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@EqualsAndHashCode
public class Route {
    private String airportFrom;
    private String airportTo;
    private String connectingAirport;
    private Boolean newRoute;
    private Boolean seasonalRoute;
    private String operator;
    private String group;
}
