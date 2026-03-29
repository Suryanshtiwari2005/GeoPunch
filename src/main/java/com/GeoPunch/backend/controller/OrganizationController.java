package com.GeoPunch.backend.controller;

import com.GeoPunch.backend.dto.ApiResponse;
import com.GeoPunch.backend.dto.organization.OrganizationRequestDto;
import com.GeoPunch.backend.dto.organization.OrganizationResponseDto;
import com.GeoPunch.backend.service.organization.OrganizationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/organizations")
@RequiredArgsConstructor
public class OrganizationController {

    private final OrganizationService organizationService;

    @PostMapping
    public ResponseEntity<ApiResponse<OrganizationResponseDto>> createOrganization(
            @Valid @RequestBody OrganizationRequestDto request) {

        OrganizationResponseDto response = organizationService.createOrganization(request);

        return ResponseEntity.ok(
                ApiResponse.<OrganizationResponseDto>builder()
                        .success(true)
                        .message("Organization created successfully")
                        .data(response)
                        .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrganizationResponseDto>> getOrganization(
            @PathVariable UUID id) {

        OrganizationResponseDto response = organizationService.getOrganizationById(id);

        return ResponseEntity.ok(
                ApiResponse.<OrganizationResponseDto>builder()
                        .success(true)
                        .message("Organization fetched successfully")
                        .data(response)
                        .build()
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<OrganizationResponseDto>>> getAllOrganizations() {

        List<OrganizationResponseDto> response = organizationService.getAllOrganizations();

        return ResponseEntity.ok(
                ApiResponse.<List<OrganizationResponseDto>>builder()
                        .success(true)
                        .message("Organizations fetched successfully")
                        .data(response)
                        .build()
        );
    }
}
