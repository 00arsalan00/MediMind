package com.medimind.repository;

import com.medimind.entity.DoctorLeave;
import com.medimind.entity.DoctorProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public interface DoctorLeaveRepository extends JpaRepository<DoctorLeave, UUID> {
    Optional<DoctorLeave> findByDoctorAndLeaveDate(DoctorProfile doctor, LocalDate leaveDate);
}
