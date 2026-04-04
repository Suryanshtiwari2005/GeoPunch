package com.GeoPunch.backend.controller;

import com.GeoPunch.backend.Entity.OfficeLocation;
import com.GeoPunch.backend.Entity.Organization;
import com.GeoPunch.backend.Entity.User;
import com.GeoPunch.backend.Enum.Roles;
import com.GeoPunch.backend.dto.ApiResponse;
import com.GeoPunch.backend.dto.RegisterRequestDto;
import com.GeoPunch.backend.exception.ResourceNotFoundException;
import com.GeoPunch.backend.repository.OfficeLocationRepository;
import com.GeoPunch.backend.repository.OrganizationRepository;
import com.GeoPunch.backend.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final OrganizationRepository organizationRepository;
    private final OfficeLocationRepository officeLocationRepository;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> register(
            @Valid @RequestBody RegisterRequestDto request) {

        // ✅ Firebase UID comes from the token — already verified by FirebaseAuthFilter
        String firebaseUid = (String) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        // Prevent duplicate registration
        if (userRepository.findByFirebaseUid(firebaseUid).isPresent()) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.<Void>builder()
                            .success(false)
                            .message("User already registered")
                            .build()
            );
        }

        Organization organization = organizationRepository.findById(request.getOrganizationId())
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found"));

        OfficeLocation office = officeLocationRepository.findById(request.getOfficeId())
                .orElseThrow(() -> new ResourceNotFoundException("Office not found"));

        if (!office.getOrganization().getId().equals(organization.getId())) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.<Void>builder()
                            .success(false)
                            .message("Office does not belong to this organization")
                            .build()
            );
        }

        User user = User.builder()
                .firebaseUid(firebaseUid)
                .name(request.getName())
                .email(request.getEmail())
                .role(Roles.EMPLOYEE)
                .organization(organization)
                .office(office)
                .build();

        userRepository.save(user);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("User registered successfully")
                        .build()
        );
    }
}