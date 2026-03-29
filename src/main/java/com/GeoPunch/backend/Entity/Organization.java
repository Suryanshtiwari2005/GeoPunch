package com.GeoPunch.backend.Entity;

import com.GeoPunch.backend.Enum.Status;
import com.GeoPunch.backend.Enum.SubscriptionPlan;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "organizations",
        indexes = {
                @Index(name = "idx_org_name", columnList = "name")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Organization {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false,length = 200)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private SubscriptionPlan subscriptionPlan;

    @Enumerated(EnumType.STRING)
    @Column(length = 50,nullable = false)
    private Status status;

    @Column(nullable = false,updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

}
