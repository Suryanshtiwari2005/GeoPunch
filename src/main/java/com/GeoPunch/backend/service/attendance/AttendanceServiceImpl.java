package com.GeoPunch.backend.service.attendance;

import com.GeoPunch.backend.Entity.Attendance;
import com.GeoPunch.backend.Entity.OfficeLocation;
import com.GeoPunch.backend.Entity.User;
import com.GeoPunch.backend.Enum.AttendanceStatus;
import com.GeoPunch.backend.dto.LocationCheckRequestDto;
import com.GeoPunch.backend.dto.attendance.AttendanceResponseDto;
import com.GeoPunch.backend.exception.BusinessException;
import com.GeoPunch.backend.exception.ResourceNotFoundException;
import com.GeoPunch.backend.repository.AttendanceRepository;
import com.GeoPunch.backend.repository.UserRepository;
import com.GeoPunch.backend.util.GeoUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final UserRepository userRepository;

    @Override
    public AttendanceResponseDto locationCheck(LocationCheckRequestDto request) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        String firebaseUid = (String) auth.getPrincipal();

        User user = userRepository.findByFirebaseUid(firebaseUid)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        OfficeLocation office = user.getOffice();

        if(office == null){
            throw new ResourceNotFoundException("No Office Location assigned to this user");
        }

        double distance = GeoUtils.calculateDistance(
                request.getLatitude(),
                request.getLongitude(),
                office.getLatitude(),
                office.getLongitude()
        );

        double entryRadius = office.getRadius();
        double exitRadius = office.getRadius() + 5; // buffer

        boolean isInside = distance <= entryRadius;
        boolean isOutside = distance > exitRadius;

        log.info("📏 Distance: {}", distance);
        log.info("📌 Entry Radius: {}", entryRadius);
        log.info("📌 Exit Radius: {}", exitRadius);
        log.info("📍 Inside: {}", isInside);
        log.info("📍 Outside: {}", isOutside);

        LocalDate today = LocalDate.now();

        Optional<Attendance> existing =
                attendanceRepository.findByUserIdAndDate(user.getId(), today);

        // 🟢 ENTRY
        if (isInside) {

            if (existing.isPresent()) {
                return mapToResponse(existing.get());
            }

            Attendance attendance = Attendance.builder()
                    .user(user)
                    .organization(user.getOrganization())
                    .officeLocation(office)
                    .date(today)
                    .checkInTime(LocalDateTime.now())
                    .status(AttendanceStatus.PRESENT)
                    .build();

            Attendance saved = attendanceRepository.save(attendance);

            log.info("🟢 User checked in");

            return mapToResponse(saved);
        }

        // 🔴 EXIT (only if clearly outside buffer)
        if (isOutside && existing.isPresent()) {

            Attendance attendance = existing.get();

            if (attendance.getCheckOutTime() == null) {

                attendance.setCheckOutTime(LocalDateTime.now());

                long minutes = Duration.between(
                        attendance.getCheckInTime(),
                        attendance.getCheckOutTime()
                ).toMinutes();

                attendance.setTotalDurationInMinutes(minutes);

                attendanceRepository.save(attendance);

                log.info("🔴 User checked out");
            }

            return mapToResponse(attendance);
        }

        // 🟡 IN BUFFER ZONE (do nothing)
        if (existing.isPresent()) {
            return mapToResponse(existing.get());
        }

        throw new BusinessException("User outside office geofence");
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
