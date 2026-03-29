package com.GeoPunch.backend.service.office;

import com.GeoPunch.backend.Entity.OfficeLocation;
import com.GeoPunch.backend.Entity.Organization;
import com.GeoPunch.backend.dto.office.OfficeRequestDto;
import com.GeoPunch.backend.dto.office.OfficeResponseDto;
import com.GeoPunch.backend.exception.ResourceNotFoundException;
import com.GeoPunch.backend.repository.OfficeLocationRepository;
import com.GeoPunch.backend.repository.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OfficeServiceImpl implements OfficeService {

    private final OfficeLocationRepository officeRepository;
    private final OrganizationRepository organizationRepository;

    @Override
    public OfficeResponseDto createOffice(OfficeRequestDto request) {

        // 🔒 Validate organization exists
        Organization organization = organizationRepository.findById(request.getOrganizationId())
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found"));

        OfficeLocation office = OfficeLocation.builder()
                .name(request.getName())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .radius(request.getRadius())
                .organization(organization)
                .build();

        OfficeLocation saved = officeRepository.save(office);

        return mapToResponse(saved);
    }

    @Override
    public OfficeResponseDto getOfficeById(UUID id, UUID organizationId) {

        OfficeLocation office = officeRepository
                .findByIdAndOrganizationId(id, organizationId)
                .orElseThrow(() -> new ResourceNotFoundException("Office not found"));

        return mapToResponse(office);
    }

    @Override
    public List<OfficeResponseDto> getOfficesByOrganization(UUID organizationId) {

        return officeRepository.findByOrganizationId(organizationId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<OfficeResponseDto> searchOffices(UUID organizationId, String name) {

        return officeRepository
                .findByOrganizationIdAndNameContainingIgnoreCase(organizationId, name)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // 🔁 Mapper
    private OfficeResponseDto mapToResponse(OfficeLocation office) {
        return OfficeResponseDto.builder()
                .id(office.getId())
                .name(office.getName())
                .latitude(office.getLatitude())
                .longitude(office.getLongitude())
                .radius(office.getRadius())
                .organizationId(office.getOrganization().getId())
                .createdAt(office.getCreatedAt())
                .build();
    }
}
