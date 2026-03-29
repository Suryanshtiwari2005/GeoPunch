package com.GeoPunch.backend.repository;

import com.GeoPunch.backend.Entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AttendanceRepository extends JpaRepository<Attendance, UUID> {

    Optional<Attendance> findByUserIdAndDate(UUID userId, LocalDate date);

    List<Attendance> findByUserId(UUID userId);

    List<Attendance> findByOrganizationIdAndDate(UUID organizationId, LocalDate date);

    List<Attendance> findByOrganizationIdAndDateBetween(
            UUID organizationId,
            LocalDate startDate,
            LocalDate endDate
    );

    long countByOrganizationIdAndDate(UUID organizationId, LocalDate date);
}
