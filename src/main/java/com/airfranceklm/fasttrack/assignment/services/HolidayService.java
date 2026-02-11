package com.airfranceklm.fasttrack.assignment.services;

import com.airfranceklm.fasttrack.assignment.resources.holiday.Holiday;

import java.util.List;
import java.util.UUID;

public interface HolidayService {
    void deleteHoliday(UUID holidayId) throws IllegalArgumentException;
    List<Holiday> getHolidaysByEmployeeId(String employeeId);
    Holiday createHoliday(Holiday holiday);
}
