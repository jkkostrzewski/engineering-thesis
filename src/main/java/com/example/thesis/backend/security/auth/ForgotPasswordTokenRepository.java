package com.example.thesis.backend.security.auth;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ForgotPasswordTokenRepository extends JpaRepository<ForgotPasswordToken, UUID> {
}
