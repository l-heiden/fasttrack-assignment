package com.airfranceklm.fasttrack.assignment.services;

import com.airfranceklm.fasttrack.assignment.resources.employee.Employee;
import com.airfranceklm.fasttrack.assignment.resources.employee.EmployeeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;

    public Employee createEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }
}