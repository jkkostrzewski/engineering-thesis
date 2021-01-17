package com.example.thesis.backend.notice;

import com.example.thesis.backend.security.auth.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import javax.validation.constraints.Size;

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

    @Size(max = 350)
    private String body;

    public Comment(User user, Instant creationDate, String body) {
        this.user = user;
        this.creationDate = creationDate;
        this.body = body;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Comment comment = (Comment) o;

        return id == comment.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}
