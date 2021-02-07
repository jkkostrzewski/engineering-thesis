package com.example.thesis.backend.reservation;

import com.example.thesis.backend.security.auth.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Reservation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private LocalDateTime start;
    private Duration duration;
    private Instant creationTime;

    @OneToOne
    private Property property;

    @OneToOne
    private User user;

    @Override
    public String toString() {
        return user.getFullName() + " " + start.toString() + " - " + start.plus(duration).toString();
    }
}
