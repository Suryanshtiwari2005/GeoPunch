package com.GeoPunch.demo.service.attendance;

import com.GeoPunch.demo.dto.attendance.AttendanceResponseDto;
import com.GeoPunch.demo.dto.attendance.ManualCheckInRequestDto;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface AttendanceService {

    AttendanceResponseDto manualCheckIn(ManualCheckInRequestDto request);

    List<AttendanceResponseDto> getAttendanceByUser(UUID userId);

    List<AttendanceResponseDto> getAttendanceByOrganizationAndDate(UUID organizationId, LocalDate date);
}
