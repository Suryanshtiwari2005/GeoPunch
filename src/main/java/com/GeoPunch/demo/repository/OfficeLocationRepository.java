package com.GeoPunch.demo.repository;

import com.GeoPunch.demo.Entity.OfficeLocation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OfficeLocationRepository extends JpaRepository<OfficeLocation, UUID> {

    List<OfficeLocation> findByOrganizationId(UUID organizationId);

    Optional<OfficeLocation> findByIdAndOrganizationId(UUID id, UUID organizationId);

    List<OfficeLocation> findByOrganizationIdAndNameContainingIgnoreCase(UUID organizationId, String name);
}
