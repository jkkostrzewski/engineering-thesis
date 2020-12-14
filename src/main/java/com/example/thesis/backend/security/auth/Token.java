package com.example.thesis.backend.security.auth;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
public class Token {

    @Id
    private UUID uuid;

    private String role;

    private boolean enabled;

    public Token(String role) {
        this.uuid = UUID.randomUUID();
        this.role = role;
        this.enabled = true;
    }

    public void disable() {
        enabled = false;
    }

    public boolean isDisabled() {
        return !enabled;
    }
}
