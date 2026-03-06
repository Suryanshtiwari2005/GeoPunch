package com.GeoPunch.demo.dto.organization;

import com.GeoPunch.demo.Enum.Status;
import com.GeoPunch.demo.Enum.SubscriptionPlan;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.antlr.v4.runtime.misc.NotNull;

@Data
public class OrganizationRequestDto {

    @NotBlank
    private String name;

    @NotNull
    private SubscriptionPlan subscriptionPlan;

    @NotNull
    private Status status;
}
