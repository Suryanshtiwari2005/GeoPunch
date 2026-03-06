package com.GeoPunch.demo.dto.attendance;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class ManualCheckInRequestDto {

    @NotNull
    private UUID userId;

    @NotNull
    private UUID officeId;

    @NotNull
    private UUID organizationId;
}
