package com.GeoPunch.backend.controller;

import com.GeoPunch.backend.dto.ApiResponse;
import com.GeoPunch.backend.dto.LocationCheckRequestDto;
import com.GeoPunch.backend.dto.attendance.AttendanceResponseDto;
import com.GeoPunch.backend.service.attendance.AttendanceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;

//    @PostMapping("/manual-checkin")
//    public ResponseEntity<ApiResponse<AttendanceResponseDto>> manualCheckIn(
//            @Valid @RequestBody ManualCheckInRequestDto request) {
//
//        AttendanceResponseDto response =
//                attendanceService.manualCheckIn(request);
//
//        return ResponseEntity.ok(
//                ApiResponse.<AttendanceResponseDto>builder()
//                        .success(true)
//                        .message("Attendance marked successfully")
//                        .data(response)
//                        .build()
//        );
//    }

    @PostMapping("/location-check")
    public ResponseEntity<ApiResponse<AttendanceResponseDto>> locationCheck(
            @Valid @RequestBody LocationCheckRequestDto request) {

        AttendanceResponseDto response =
                attendanceService.locationCheck(request);

        return ResponseEntity.ok(
                ApiResponse.<AttendanceResponseDto>builder()
                        .success(true)
                        .message("Location processed successfully")
                        .data(response)
                        .build()
        );
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<AttendanceResponseDto>>> getAttendanceByUser(
            @PathVariable UUID userId) {

        List<AttendanceResponseDto> response =
                attendanceService.getAttendanceByUser(userId);

        return ResponseEntity.ok(
                ApiResponse.<List<AttendanceResponseDto>>builder()
                        .success(true)
                        .message("Attendance fetched successfully")
                        .data(response)
                        .build()
        );
    }

    @GetMapping("/organization/{organizationId}")
    public ResponseEntity<ApiResponse<List<AttendanceResponseDto>>> getAttendanceByOrgAndDate(
            @PathVariable UUID organizationId,
            @RequestParam LocalDate date) {

        List<AttendanceResponseDto> response =
                attendanceService.getAttendanceByOrganizationAndDate(organizationId, date);

        return ResponseEntity.ok(
                ApiResponse.<List<AttendanceResponseDto>>builder()
                        .success(true)
                        .message("Attendance fetched successfully")
                        .data(response)
                        .build()
        );
    }
}
