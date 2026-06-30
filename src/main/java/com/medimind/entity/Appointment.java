package com.medimind.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "appointments", 
       uniqueConstraints = @UniqueConstraint(columnNames = {"doctor_id", "appointment_date_time"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Appointment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private PatientProfile patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private DoctorProfile doctor;

    @Column(name = "appointment_date_time", nullable = false)
    private LocalDateTime appointmentDateTime;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private AppointmentStatus status = AppointmentStatus.PENDING;

    @Column(columnDefinition = "TEXT")
    private String patientSymptoms;

    @Enumerated(EnumType.STRING)
    private UrgencyLevel urgencyLevel;

    @Column(columnDefinition = "TEXT")
    private String preVisitSummary;

    @Column(columnDefinition = "TEXT")
    private String doctorClinicalNotes;

    @Column(columnDefinition = "TEXT")
    private String patientFriendlySummary;

    private String googleCalendarEventId;
}
