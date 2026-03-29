package com.GeoPunch.backend.dto.user;

import com.GeoPunch.backend.Enum.Roles;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class UserResponseDto {

    private UUID id;
    private String name;
    private String email;
    private Roles role;
    private UUID organizationId;
    private UUID officeId;
    private LocalDateTime createdAt;
}
