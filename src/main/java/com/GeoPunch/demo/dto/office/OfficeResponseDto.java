package com.GeoPunch.demo.dto.office;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class OfficeResponseDto {

    private UUID id;
    private String name;
    private Double latitude;
    private Double longitude;
    private Double radius;
    private UUID organizationId;
    private LocalDateTime createdAt;
}
