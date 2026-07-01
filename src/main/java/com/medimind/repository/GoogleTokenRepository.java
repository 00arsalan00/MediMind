package com.medimind.repository;

import com.medimind.entity.GoogleToken;
import com.medimind.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface GoogleTokenRepository extends JpaRepository<GoogleToken, UUID> {
    Optional<GoogleToken> findByUser(User user);
}
