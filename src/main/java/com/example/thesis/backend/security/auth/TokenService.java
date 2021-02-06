package com.example.thesis.backend.security.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TokenService {

    private final TokenRepository tokenRepository;
    private final ForgotPasswordTokenRepository forgotPasswordTokenRepository;

    @Autowired
    public TokenService(TokenRepository tokenRepository,
                        ForgotPasswordTokenRepository forgotPasswordTokenRepository) {
        this.tokenRepository = tokenRepository;
        this.forgotPasswordTokenRepository = forgotPasswordTokenRepository;
    }

    public void saveRegistrationToken(RegistrationToken token) {
        tokenRepository.save(token);
    }

    public void saveForgotPasswordToken(ForgotPasswordToken token) {
        forgotPasswordTokenRepository.save(token);
    }

    public ForgotPasswordToken findByTokenId(UUID uuid) {
        return forgotPasswordTokenRepository.findById(uuid).get();
    }
}
