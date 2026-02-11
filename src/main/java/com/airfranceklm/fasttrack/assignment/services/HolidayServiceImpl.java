package com.airfranceklm.fasttrack.assignment.services;

import com.airfranceklm.fasttrack.assignment.resources.holiday.Holiday;
import com.airfranceklm.fasttrack.assignment.resources.holiday.HolidayRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HolidayServiceImpl implements HolidayService {
    private final HolidayRepository holidayRepository;

    public Holiday createHoliday(Holiday holiday) {
        return holidayRepository.save(holiday);
    }

    public List<Holiday> getHolidaysByEmployeeId(String employeeId) {
        return holidayRepository.findByEmployeeId(employeeId);
    }
}