package com.GeoPunch.demo.repository;

import com.GeoPunch.demo.Entity.Organization;
import com.GeoPunch.demo.Enum.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrganizationRepository extends JpaRepository<Organization, UUID> {

    Optional<Organization> findByName(String name);

    List<Organization> findByStatus(Status status);
}