package com.home.dev.ifs.model;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class ScheduleRequestDto {
    private String departure;
    private String arrival;
    private int year;
    private int month;
}
