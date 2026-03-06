package com.GeoPunch.demo.Entity;

import com.GeoPunch.demo.Enum.AttendanceStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
        name="attendance",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id","date"})
        },
        indexes = {
                @Index(name = "idx_attendance_user", columnList = "user_id"),
                @Index(name = "idx_attendance_org", columnList = "organization_id"),
                @Index(name = "idx_attendance_date", columnList = "date")
        }

)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column
    private LocalDateTime checkOutTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AttendanceStatus status;

    @Column(nullable = false)
    private LocalDate date;

    @Column
    private Long totalDurationInMinutes;

    @Column(nullable = false,length=100)
    private LocalDateTime checkInTime;

    @PrePersist
    protected void onCreate(){
        this.checkInTime = LocalDateTime.now();
    }

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "organization_id",nullable = false)
    private Organization organization;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "office_location_id",nullable = false)
    private OfficeLocation officeLocation;

}
