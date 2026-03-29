package com.GeoPunch.backend.service.organization;

import com.GeoPunch.backend.Entity.Organization;
import com.GeoPunch.backend.dto.organization.OrganizationRequestDto;
import com.GeoPunch.backend.dto.organization.OrganizationResponseDto;
import com.GeoPunch.backend.exception.DuplicateResourceException;
import com.GeoPunch.backend.exception.ResourceNotFoundException;
import com.GeoPunch.backend.repository.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class OrganizationServiceImpl implements OrganizationService {

    private final OrganizationRepository organizationRepository;

    @Override
    public OrganizationResponseDto createOrganization(OrganizationRequestDto request) {

        // 🔒 Check duplicate name
        organizationRepository.findByName(request.getName())
                .ifPresent(org -> {
                    throw new DuplicateResourceException("Organization with this name already exists");
                });

        Organization organization = Organization.builder()
                .name(request.getName())
                .subscriptionPlan(request.getSubscriptionPlan())
                .status(request.getStatus())
                .build();

        Organization saved = organizationRepository.save(organization);

        return mapToResponse(saved);
    }

    @Override
    public OrganizationResponseDto getOrganizationById(UUID id) {

        Organization organization = organizationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found"));

        return mapToResponse(organization);
    }

    @Override
    public List<OrganizationResponseDto> getAllOrganizations() {

        return organizationRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // 🔁 Mapper method
    private OrganizationResponseDto mapToResponse(Organization organization) {
        return OrganizationResponseDto.builder()
                .id(organization.getId())
                .name(organization.getName())
                .subscriptionPlan(organization.getSubscriptionPlan())
                .status(organization.getStatus())
                .createdAt(organization.getCreatedAt())
                .build();
    }
}
