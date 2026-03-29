package com.GeoPunch.backend.service.attendance;

import com.GeoPunch.backend.dto.LocationCheckRequestDto;
import com.GeoPunch.backend.dto.attendance.AttendanceResponseDto;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface AttendanceService {

//    AttendanceResponseDto manualCheckIn(ManualCheckInRequestDto request);

    AttendanceResponseDto locationCheck(LocationCheckRequestDto request);

    List<AttendanceResponseDto> getAttendanceByUser(UUID userId);

    List<AttendanceResponseDto> getAttendanceByOrganizationAndDate(UUID organizationId, LocalDate date);
}
