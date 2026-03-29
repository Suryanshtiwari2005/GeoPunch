package com.GeoPunch.backend.controller;

import com.GeoPunch.backend.dto.ApiResponse;
import com.GeoPunch.backend.dto.office.OfficeRequestDto;
import com.GeoPunch.backend.dto.office.OfficeResponseDto;
import com.GeoPunch.backend.service.office.OfficeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/offices")
@RequiredArgsConstructor
public class OfficeController {

    private final OfficeService officeService;

    @PostMapping
    public ResponseEntity<ApiResponse<OfficeResponseDto>> createOffice(
            @Valid @RequestBody OfficeRequestDto request) {

        OfficeResponseDto response = officeService.createOffice(request);

        return ResponseEntity.ok(
                ApiResponse.<OfficeResponseDto>builder()
                        .success(true)
                        .message("Office created successfully")
                        .data(response)
                        .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OfficeResponseDto>> getOffice(
            @PathVariable UUID id,
            @RequestParam UUID organizationId) {

        OfficeResponseDto response =
                officeService.getOfficeById(id, organizationId);

        return ResponseEntity.ok(
                ApiResponse.<OfficeResponseDto>builder()
                        .success(true)
                        .message("Office fetched successfully")
                        .data(response)
                        .build()
        );
    }

    @GetMapping("/organization/{organizationId}")
    public ResponseEntity<ApiResponse<List<OfficeResponseDto>>> getOfficesByOrganization(
            @PathVariable UUID organizationId) {

        List<OfficeResponseDto> response =
                officeService.getOfficesByOrganization(organizationId);

        return ResponseEntity.ok(
                ApiResponse.<List<OfficeResponseDto>>builder()
                        .success(true)
                        .message("Offices fetched successfully")
                        .data(response)
                        .build()
        );
    }
}
