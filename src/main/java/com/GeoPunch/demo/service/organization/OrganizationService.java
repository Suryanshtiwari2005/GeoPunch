package com.GeoPunch.demo.service.organization;

import com.GeoPunch.demo.dto.organization.OrganizationRequestDto;
import com.GeoPunch.demo.dto.organization.OrganizationResponseDto;

import java.util.List;
import java.util.UUID;

public interface OrganizationService {

    OrganizationResponseDto createOrganization(OrganizationRequestDto request);

    OrganizationResponseDto getOrganizationById(UUID id);

    List<OrganizationResponseDto> getAllOrganizations();
}
