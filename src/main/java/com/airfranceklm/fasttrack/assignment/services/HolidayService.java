package com.airfranceklm.fasttrack.assignment.services;

import com.airfranceklm.fasttrack.assignment.resources.holiday.Holiday;

import java.util.List;

public interface HolidayService {
    Holiday createHoliday(Holiday holiday);

    List<Holiday> getHolidaysByEmployeeId(String employeeId);
}
