package com.airfranceklm.fasttrack.assignment.resources.employee;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Pattern;
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
    @Pattern(regexp = "^klm\\d{6}$", message = "employeeId must match klm012345")
    private String employeeId;

    @Column(name = "name", nullable = false)
    private String name;
}