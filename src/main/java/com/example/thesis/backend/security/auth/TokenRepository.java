package com.example.thesis.backend.security.auth;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TokenRepository extends JpaRepository<Token, UUID> {
    Token findByUuid(UUID uuid);
}
