package com.home.dev.ifs.service.schedule;

import com.home.dev.ifs.model.Schedule;
import com.home.dev.ifs.model.ScheduleRequestDto;

import java.util.List;

public interface ScheduleConsumer {
    List<Schedule> getSchedulesForGivenMonth(ScheduleRequestDto scheduleRequest);
}
