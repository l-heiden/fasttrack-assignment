package com.airfranceklm.fasttrack.assignment.services;

import com.airfranceklm.fasttrack.assignment.resources.employee.EmployeeRepository;
import com.airfranceklm.fasttrack.assignment.resources.holiday.Holiday;
import com.airfranceklm.fasttrack.assignment.resources.holiday.HolidayRepository;
import com.airfranceklm.fasttrack.assignment.resources.holiday.HolidayStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HolidayServiceImpl implements HolidayService {

    private final HolidayRepository holidayRepository;
    private final EmployeeRepository employeeRepository;

    public List<Holiday> getHolidaysByEmployeeId(String employeeId) {
        return holidayRepository.findByEmployeeId(employeeId);
    }

    public void deleteHoliday(UUID id) throws IllegalArgumentException {
        Holiday holiday = holidayRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Holiday not found"));

        validateBusinessDaysBeforeStart(holiday);
        holidayRepository.delete(holiday);
    }

    public Holiday createHoliday(Holiday holiday) {
        validateEmployeeExists(holiday.getEmployeeId());
        validateBusinessDaysBeforeStart(holiday);
        validateStartDateIsAfterEndDate(holiday);
        validateGapBetweenHolidays(holiday, holiday.getEmployeeId());

        holiday.setStatus(HolidayStatus.SCHEDULED);
        return holidayRepository.save(holiday);
    }

    //Business rule: holiday must belong to an existing employee
    private void validateEmployeeExists(String employeeId) {
        if (!employeeRepository.existsById(employeeId)) {
            throw new IllegalArgumentException("Employee does not exist");
        }
    }

    //Business rule: creating or cancelling a holiday is after 5 business days
    private void validateBusinessDaysBeforeStart(Holiday holiday) {
        LocalDate today = LocalDate.now();
        LocalDate start = holiday.getStartOfHoliday().toLocalDate();

        if (!hasWorkingDayGap(today, start, 5)) {
            throw new IllegalArgumentException("Holiday must be requested at least 5 working days in advance");
        }
    }

    //Business rule: end date must be after starting date
    private void validateStartDateIsAfterEndDate(Holiday holiday) {
        if (holiday.getEndOfHoliday().isBefore(holiday.getStartOfHoliday())) {
            throw new IllegalArgumentException("End date cannot be before start date");
        }
    }

    //Business rule: there must be at least 3 working days between holidays
    private void validateGapBetweenHolidays(Holiday holiday, String employeeId) {
        List<Holiday> existing = holidayRepository.findByEmployeeId(employeeId);

        for (Holiday h : existing) {
            LocalDate end = h.getEndOfHoliday().toLocalDate();
            LocalDate start = holiday.getStartOfHoliday().toLocalDate();

            if (!hasWorkingDayGap(end, start, 3)) {
                throw new IllegalArgumentException("There must be at least 3 working days between holidays");
            }
        }
    }

    // Check how many working days between two dates
    private boolean hasWorkingDayGap(LocalDate end, LocalDate start, int workingDays) {
        int count = 0;
        LocalDate date = end;

        while (count < workingDays) {
            date = date.plusDays(1);
            if (isWorkingDay(date)) {
                count++;
            }
        }

        return !date.isAfter(start);
    }

    // Check if a date is a working day
    private boolean isWorkingDay(LocalDate date) {
        return !(date.getDayOfWeek() == DayOfWeek.SATURDAY ||
                date.getDayOfWeek() == DayOfWeek.SUNDAY);
    }
}
