package com.GeoPunch.demo.Entity;

import com.GeoPunch.demo.Enum.Roles;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "users",
        indexes = {
                @Index(name = "idx_user_org", columnList = "organization_id"),
                @Index(name = "idx_user_email", columnList = "email"),
                @Index(name = "idx_user_firebase", columnList = "firebase_uid")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(length = 150,nullable = false)
    private String name;

    @Column(unique = true,length=150,nullable = false)
    private String email;

    @Column(name = "firebase_uid",unique = true,nullable = false,length=128)
    private String firebaseUid;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false,length = 20)
    private Roles role;

    @Column(nullable = false,updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "organization_id",nullable = false)
    private Organization organization;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

}
