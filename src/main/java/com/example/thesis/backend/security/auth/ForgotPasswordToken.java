package com.example.thesis.backend.security.auth;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
public class ForgotPasswordToken {

    @Id
    private UUID uuid;

    @Email
    private String email;

    private boolean enabled;

    public ForgotPasswordToken(@Email String email) {
        this.uuid = UUID.randomUUID();
        this.email = email;
        this.enabled = true;
    }

    public void disable() {
        enabled = false;
    }
}
