package com.example.thesis.backend.notice;

import com.example.thesis.backend.security.auth.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.GenerationType.AUTO;

@Entity
@Data
@NoArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = AUTO)
    private long id;

    @OneToOne
    private User user;

    private Instant creationDate;

    private String body;

    public Comment(User user, Instant creationDate, String body) {
        this.user = user;
        this.creationDate = creationDate;
        this.body = body;
    }
}
