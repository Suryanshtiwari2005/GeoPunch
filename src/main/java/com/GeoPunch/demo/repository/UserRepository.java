package com.GeoPunch.demo.repository;

import com.GeoPunch.demo.Entity.User;
import com.GeoPunch.demo.Enum.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByFirebaseUid(String firebaseUid);

    Optional<User> findByEmail(String email);

    List<User> findByOrganizationId(UUID organizationId);

    List<User> findByOrganizationIdAndRole(UUID organizationId, Roles role);

    long countByOrganizationId(UUID organizationId);
}
