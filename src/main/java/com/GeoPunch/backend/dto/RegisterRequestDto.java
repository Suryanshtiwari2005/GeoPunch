package com.GeoPunch.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class RegisterRequestDto {

    @NotBlank
    private String name;

    @NotBlank
    private String email;

    @NotNull
    private UUID organizationId;

    @NotNull
    private UUID officeId;
}
