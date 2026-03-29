package com.GeoPunch.backend.dto.attendance;

import com.GeoPunch.backend.Enum.AttendanceStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class AttendanceResponseDto {

    private UUID id;
    private UUID userId;
    private UUID officeId;
    private UUID organizationId;
    private LocalDate date;
    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;
    private Long totalDurationInMinutes;
    private AttendanceStatus status;
}