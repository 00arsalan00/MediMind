package com.medimind.repository;

import com.medimind.entity.DoctorProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface DoctorProfileRepository extends JpaRepository<DoctorProfile, UUID> {
}
