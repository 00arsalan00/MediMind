package com.medimind.repository;

import com.medimind.entity.Appointment;
import com.medimind.entity.DoctorProfile;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT a FROM Appointment a WHERE a.doctor.id = :doctorId AND a.appointmentDateTime = :dateTime AND a.status <> com.medimind.entity.AppointmentStatus.CANCELLED")
    Optional<Appointment> findByDoctorAndDateTimeWithLock(@Param("doctorId") UUID doctorId, @Param("dateTime") LocalDateTime dateTime);

    List<Appointment> findAllByDoctorAndAppointmentDateTimeBetween(DoctorProfile doctor, LocalDateTime start, LocalDateTime end);
}
