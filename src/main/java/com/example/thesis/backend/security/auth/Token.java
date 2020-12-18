package com.example.thesis.backend.security.auth;

import com.example.thesis.backend.floor.Floor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Email;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
public class Token {

    @Id
    private UUID uuid;

    @Email
    private String email;

    @ManyToOne
    private Floor mainFloor;

    private String role;

    private boolean enabled;

    public Token(String role, String email, Floor mainFloor) {
        this.uuid = UUID.randomUUID();
        this.role = role;
        this.email = email;
        this.mainFloor = mainFloor;
        this.enabled = true;
    }

    public void disable() {
        enabled = false;
    }

    public boolean isDisabled() {
        return !enabled;
    }
}
