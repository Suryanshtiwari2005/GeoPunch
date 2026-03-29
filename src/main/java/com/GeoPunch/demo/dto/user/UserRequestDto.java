package com.GeoPunch.demo.dto.user;

import com.GeoPunch.demo.Enum.Roles;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class UserRequestDto {

    @NotBlank
    private String name;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String firebaseUid;

    @NotNull
    private Roles role;

    @NotNull
    private UUID organizationId;
}
