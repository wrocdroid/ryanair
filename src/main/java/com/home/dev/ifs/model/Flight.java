package com.home.dev.ifs.model;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Flight {
    private String number;
    private String departureTime;
    private String arrivalTime;
}
