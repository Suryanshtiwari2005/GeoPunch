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
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final UserRepository userRepository;
//    private final OfficeLocationRepository officeRepository;
//    private final OrganizationRepository organizationRepository;

//    @Override
//    public AttendanceResponseDto manualCheckIn(ManualCheckInRequestDto request) {
//
//        // 1️⃣ Validate organization
//        Organization organization = organizationRepository.findById(request.getOrganizationId())
//                .orElseThrow(() -> new ResourceNotFoundException("Organization not found"));
//
//        // 2️⃣ Validate user inside organization
//        User user = userRepository.findById(request.getUserId())
//                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
//
//        if (!user.getOrganization().getId().equals(organization.getId())) {
//            throw new BusinessException("User does not belong to this organization");
//        }
//
//        // 3️⃣ Validate office inside organization
//        OfficeLocation office = officeRepository
//                .findByIdAndOrganizationId(request.getOfficeId(), request.getOrganizationId())
//                .orElseThrow(() -> new ResourceNotFoundException("Office not found in organization"));
//
//        // 4️⃣ Prevent duplicate attendance
//        LocalDate today = LocalDate.now();
//
//        attendanceRepository.findByUserIdAndDate(user.getId(), today)
//                .ifPresent(a -> {
//                    throw new DuplicateResourceException("Attendance already marked for today");
//                });
//
//        // 5️⃣ Create attendance
//        Attendance attendance = Attendance.builder()
//                .user(user)
//                .officeLocation(office)
//                .organization(organization)
//                .date(today)
//                .status(AttendanceStatus.PRESENT)
//                .build();
//
//        Attendance saved = attendanceRepository.save(attendance);
//
//        return mapToResponse(saved);
//    }

    @Override
    public AttendanceResponseDto locationCheck(LocationCheckRequestDto request) {

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        OfficeLocation office = user.getOffice();

        double distance = GeoUtils.calculateDistance(
                request.getLatitude(),
                request.getLongitude(),
                office.getLatitude(),
                office.getLongitude()
        );

        LocalDate today = LocalDate.now();

        Optional<Attendance> existing =
                attendanceRepository.findByUserIdAndDate(user.getId(), today);

        if (distance <= office.getRadius() + 10) {

            if (existing.isPresent()) {
                return mapToResponse(existing.get());
            }

            Attendance attendance = Attendance.builder()
                    .user(user)
                    .organization(user.getOrganization())
                    .officeLocation(office)
                    .date(today)
                    .status(AttendanceStatus.PRESENT)
                    .build();

            Attendance saved = attendanceRepository.save(attendance);

            return mapToResponse(saved);
        }

        if (existing.isPresent()) {

            Attendance attendance = existing.get();

            if (attendance.getCheckOutTime() == null) {

                attendance.setCheckOutTime(LocalDateTime.now());

                long minutes = Duration.between(
                        attendance.getCheckInTime(),
                        attendance.getCheckOutTime()
                ).toMinutes();

                attendance.setTotalDurationInMinutes(minutes);

                attendanceRepository.save(attendance);
            }

            return mapToResponse(attendance);
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
