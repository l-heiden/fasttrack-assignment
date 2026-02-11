package com.airfranceklm.fasttrack.assignment.controller;

import com.airfranceklm.fasttrack.assignment.resources.holiday.Holiday;
import com.airfranceklm.fasttrack.assignment.services.HolidayService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/holidays")
@RequiredArgsConstructor
public class HolidaysApi {

    private final HolidayService holidayService;

    @RequestMapping(value = "/{employeeId}", method = RequestMethod.GET)
    public ResponseEntity<List<Holiday>> getHolidays(@PathVariable String employeeId) {
        return new ResponseEntity<>(holidayService.getHolidaysByEmployeeId(employeeId), HttpStatus.OK);
    }

    @DeleteMapping("/{holidayId}")
    public void deleteHoliday(@PathVariable("holidayId") UUID uuid) {
        holidayService.deleteHoliday(uuid);
    }

    @PostMapping
    public Holiday createHoliday(@RequestBody Holiday holiday) {
        return holidayService.createHoliday(holiday);
    }
}