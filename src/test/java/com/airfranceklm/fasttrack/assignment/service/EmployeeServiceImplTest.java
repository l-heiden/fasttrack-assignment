package com.airfranceklm.fasttrack.assignment.service;

import com.airfranceklm.fasttrack.assignment.resources.employee.Employee;
import com.airfranceklm.fasttrack.assignment.resources.employee.EmployeeRepository;
import com.airfranceklm.fasttrack.assignment.services.EmployeeService;
import com.airfranceklm.fasttrack.assignment.services.EmployeeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class EmployeeServiceImplTest {

    private EmployeeService employeeService;

    private EmployeeRepository employeeRepository;

    @BeforeEach
    void setup() {
        employeeRepository = mock(EmployeeRepository.class);
        employeeService = new EmployeeServiceImpl(employeeRepository);
    }

    //Create employee: happy_flow
    @Test
    void createEmployee_savesSuccessfully() {
        Employee employee = Employee.builder()
                .employeeId("KLM012345")
                .name("Jan de Jong")
                .build();

        when(employeeRepository.save(any(Employee.class))).thenAnswer(inv -> inv.getArgument(0));

        Employee result = employeeService.createEmployee(employee);

        assertEquals("KLM012345", result.getEmployeeId());
        assertEquals("Jan de Jong", result.getName());
        verify(employeeRepository).save(employee);
    }
}
