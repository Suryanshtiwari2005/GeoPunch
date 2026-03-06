package com.GeoPunch.demo.service.attendance;

import com.GeoPunch.demo.Entity.Attendance;
import com.GeoPunch.demo.Entity.OfficeLocation;
import com.GeoPunch.demo.Entity.Organization;
import com.GeoPunch.demo.Entity.User;
import com.GeoPunch.demo.Enum.AttendanceStatus;
import com.GeoPunch.demo.dto.attendance.AttendanceResponseDto;
import com.GeoPunch.demo.dto.attendance.ManualCheckInRequestDto;
import com.GeoPunch.demo.exception.BusinessException;
import com.GeoPunch.demo.exception.DuplicateResourceException;
import com.GeoPunch.demo.exception.ResourceNotFoundException;
import com.GeoPunch.demo.repository.AttendanceRepository;
import com.GeoPunch.demo.repository.OfficeLocationRepository;
import com.GeoPunch.demo.repository.OrganizationRepository;
import com.GeoPunch.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final UserRepository userRepository;
    private final OfficeLocationRepository officeRepository;
    private final OrganizationRepository organizationRepository;

    @Override
    public AttendanceResponseDto manualCheckIn(ManualCheckInRequestDto request) {

        // 1️⃣ Validate organization
        Organization organization = organizationRepository.findById(request.getOrganizationId())
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found"));

        // 2️⃣ Validate user inside organization
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!user.getOrganization().getId().equals(organization.getId())) {
            throw new BusinessException("User does not belong to this organization");
        }

        // 3️⃣ Validate office inside organization
        OfficeLocation office = officeRepository
                .findByIdAndOrganizationId(request.getOfficeId(), request.getOrganizationId())
                .orElseThrow(() -> new ResourceNotFoundException("Office not found in organization"));

        // 4️⃣ Prevent duplicate attendance
        LocalDate today = LocalDate.now();

        attendanceRepository.findByUserIdAndDate(user.getId(), today)
                .ifPresent(a -> {
                    throw new DuplicateResourceException("Attendance already marked for today");
                });

        // 5️⃣ Create attendance
        Attendance attendance = Attendance.builder()
                .user(user)
                .officeLocation(office)
                .organization(organization)
                .date(today)
                .status(AttendanceStatus.PRESENT)
                .build();

        Attendance saved = attendanceRepository.save(attendance);

        return mapToResponse(saved);
    }

    @Override
    public List<AttendanceResponseDto> getAttendanceByUser(UUID userId) {

        return attendanceRepository.findByUserId(userId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<AttendanceResponseDto> getAttendanceByOrganizationAndDate(UUID organizationId, LocalDate date) {

        return attendanceRepository
                .findByOrganizationIdAndDate(organizationId, date)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private AttendanceResponseDto mapToResponse(Attendance attendance) {
        return AttendanceResponseDto.builder()
                .id(attendance.getId())
                .userId(attendance.getUser().getId())
                .officeId(attendance.getOfficeLocation().getId())
                .organizationId(attendance.getOrganization().getId())
                .date(attendance.getDate())
                .checkInTime(attendance.getCheckInTime())
                .checkOutTime(attendance.getCheckOutTime())
                .totalDurationInMinutes(attendance.getTotalDurationInMinutes())
                .status(attendance.getStatus())
                .build();
    }
}
