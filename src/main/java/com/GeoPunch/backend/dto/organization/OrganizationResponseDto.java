package com.GeoPunch.backend.dto.organization;

import com.GeoPunch.backend.Enum.Status;
import com.GeoPunch.backend.Enum.SubscriptionPlan;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class OrganizationResponseDto {

    private UUID id;
    private String name;
    private SubscriptionPlan subscriptionPlan;
    private Status status;
    private LocalDateTime createdAt;
}
