package com.airfranceklm.fasttrack.assignment.resources.employee;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "employees")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employee {
    @Id
    @Column
    private String employeeId;

    @Column(name = "name", nullable = false)
    private String name;
}