package com.example.thesis.backend.notice;

import com.example.thesis.backend.security.auth.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;

import java.time.Instant;

import static javax.persistence.GenerationType.AUTO;

@Entity
@Data
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Comment {

    @Id
    @GeneratedValue(strategy = AUTO)
    private long id;

    @ManyToOne
    @org.hibernate.annotations.Fetch(FetchMode.SELECT)
    private User user;

    private Instant creationDate;

    private String body;

    public Comment(User user, Instant creationDate, String body) {
        this.user = user;
        this.creationDate = creationDate;
        this.body = body;
    }
}
