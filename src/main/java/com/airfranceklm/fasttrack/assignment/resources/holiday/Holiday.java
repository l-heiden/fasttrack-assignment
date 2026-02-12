package com.airfranceklm.fasttrack.assignment.resources.holiday;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

@Entity
@Table(name = "holidays")
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Holiday {

    @Id
    @Column(name = "holiday_id", nullable = false, updatable = false)
    private UUID holidayId;

    @Column(name = "holiday_label", nullable = false)
    private String holidayLabel;

    @Column(name = "employee_id", nullable = false)
    private String employeeId;

    @Column(name = "start_of_holiday")
    private OffsetDateTime startOfHoliday;

    @Column(name = "end_of_holiday")
    private OffsetDateTime endOfHoliday;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private HolidayStatus status;

    @PrePersist
    public void ensureId() { holidayId = Optional.ofNullable(holidayId).orElseGet(UUID::randomUUID); }
}
