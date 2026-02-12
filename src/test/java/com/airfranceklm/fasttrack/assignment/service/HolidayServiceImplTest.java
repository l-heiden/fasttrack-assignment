package com.airfranceklm.fasttrack.assignment.service;

import com.airfranceklm.fasttrack.assignment.resources.employee.EmployeeRepository;
import com.airfranceklm.fasttrack.assignment.resources.holiday.*;
import com.airfranceklm.fasttrack.assignment.services.HolidayServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HolidayServiceImplTest {

    private HolidayRepository holidayRepository;
    private EmployeeRepository employeeRepository;
    private HolidayServiceImpl holidayService;

    @BeforeEach
    void setup() {
        holidayRepository = mock(HolidayRepository.class);
        employeeRepository = mock(EmployeeRepository.class);
        holidayService = new HolidayServiceImpl(holidayRepository, employeeRepository);
    }

    //Get holidays
    @Test
    void getHolidaysByEmployeeId_returnsList() {
        when(holidayRepository.findByEmployeeId("KLM012345"))
                .thenReturn(List.of(Holiday.builder().build()));

        List<Holiday> result = holidayService.getHolidaysByEmployeeId("KLM012345");

        assertEquals(1, result.size());
        verify(holidayRepository).findByEmployeeId("KLM012345");
    }

    //Delete holiday
    @Test
    void deleteHoliday_whenHolidayExists_deletesSuccessfully() {
        UUID id = UUID.randomUUID();
        Holiday holiday = Holiday.builder()
                .holidayId(id)
                .startOfHoliday(OffsetDateTime.now().plusDays(10))
                .build();

        when(holidayRepository.findById(id)).thenReturn(Optional.of(holiday));

        holidayService.deleteHoliday(id);

        verify(holidayRepository).delete(holiday);
    }

    //Delete holiday: throws exception when not found
    @Test
    void deleteHoliday_holidayNotFound_throwsException() {
        UUID id = UUID.randomUUID();
        when(holidayRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> holidayService.deleteHoliday(id));
    }

    //Create holiday: employee must exist
    @Test
    void createHoliday_employeeDoesNotExist_throwsException() {
        Holiday holiday = Holiday.builder()
                .employeeId("KLM012345")
                .startOfHoliday(OffsetDateTime.now().plusDays(10))
                .endOfHoliday(OffsetDateTime.now().plusDays(12))
                .build();

        when(employeeRepository.existsById("KLM012345")).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> holidayService.createHoliday(holiday));
    }

    //Create holiday: end date is before start date
    @Test
    void createHoliday_endBeforeStart_throwsException() {
        Holiday holiday = Holiday.builder()
                .employeeId("KLM012345")
                .startOfHoliday(OffsetDateTime.now().plusDays(10))
                .endOfHoliday(OffsetDateTime.now().plusDays(5))
                .build();

        when(employeeRepository.existsById("KLM012345")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> holidayService.createHoliday(holiday));
    }

    //Create holiday: nog enough working days before start
    @Test
    void createHoliday_notEnoughWorkingDaysBeforeStart_throwsException() {
        Holiday holiday = Holiday.builder()
                .employeeId("KLM012345")
                .startOfHoliday(OffsetDateTime.now().plusDays(2))
                .endOfHoliday(OffsetDateTime.now().plusDays(5))
                .build();

        when(employeeRepository.existsById("KLM012345")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> holidayService.createHoliday(holiday));
    }

    //Create holiday: gap between holidays is too small
    @Test
    void createHoliday_gapBetweenHolidaysTooSmall_throwsException() {
        Holiday existing = Holiday.builder()
                .holidayId(UUID.randomUUID())
                .employeeId("KLM012345")
                .startOfHoliday(OffsetDateTime.now().plusDays(1))
                .endOfHoliday(OffsetDateTime.now().plusDays(3))
                .build();

        Holiday newHoliday = Holiday.builder()
                .employeeId("KLM012345")
                .startOfHoliday(OffsetDateTime.now().plusDays(4))
                .endOfHoliday(OffsetDateTime.now().plusDays(6))
                .build();

        when(employeeRepository.existsById("KLM012345")).thenReturn(true);
        when(holidayRepository.findByEmployeeId("KLM012345")).thenReturn(List.of(existing));

        assertThrows(IllegalArgumentException.class, () -> holidayService.createHoliday(newHoliday));
    }

    //Create holiday: happy_flow
    @Test
    void createHoliday_savesSuccessfully() {
        Holiday holiday = Holiday.builder()
                .employeeId("KLM012345")
                .holidayLabel("Summer break")
                .startOfHoliday(OffsetDateTime.now().plusDays(10))
                .endOfHoliday(OffsetDateTime.now().plusDays(12))
                .build();

        when(employeeRepository.existsById("KLM012345")).thenReturn(true);
        when(holidayRepository.findByEmployeeId("KLM012345")).thenReturn(Collections.emptyList());
        when(holidayRepository.save(any(Holiday.class))).thenAnswer(inv -> inv.getArgument(0));

        Holiday result = holidayService.createHoliday(holiday);

        assertEquals(HolidayStatus.SCHEDULED, result.getStatus());
        verify(holidayRepository).save(holiday);
    }
}