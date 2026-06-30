package com.medimind.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "doctor_profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorProfile extends BaseEntity {

    @Id
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id")
    private User user;

    private String specialization;

    @Column(columnDefinition = "TEXT")
    private String bio;

    private LocalTime workingHoursStart;

    private LocalTime workingHoursEnd;

    private Integer slotDurationMinutes;

    @Builder.Default
    private Boolean isActive = true;
}
