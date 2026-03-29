package com.GeoPunch.demo.service.office;

import com.GeoPunch.demo.dto.office.OfficeRequestDto;
import com.GeoPunch.demo.dto.office.OfficeResponseDto;

import java.util.List;
import java.util.UUID;

public interface OfficeService {

    OfficeResponseDto createOffice(OfficeRequestDto request);

    OfficeResponseDto getOfficeById(UUID id, UUID organizationId);

    List<OfficeResponseDto> getOfficesByOrganization(UUID organizationId);

    List<OfficeResponseDto> searchOffices(UUID organizationId, String name);
}
