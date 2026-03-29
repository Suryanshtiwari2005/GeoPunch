package com.GeoPunch.backend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class LocationCheckRequestDto {

    @NotNull
    private UUID userId;

    @NotNull
    private Double latitude;

    @NotNull
    private Double longitude;
}
