package com.GeoPunch.backend.service.organization;

import com.GeoPunch.backend.dto.organization.OrganizationRequestDto;
import com.GeoPunch.backend.dto.organization.OrganizationResponseDto;

import java.util.List;
import java.util.UUID;

public interface OrganizationService {

    OrganizationResponseDto createOrganization(OrganizationRequestDto request);

    OrganizationResponseDto getOrganizationById(UUID id);

    List<OrganizationResponseDto> getAllOrganizations();
}
