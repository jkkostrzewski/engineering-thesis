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
public class ParentComment extends Comment {

    @Id
    @GeneratedValue(strategy = AUTO)
    private long id;

    @OneToMany(fetch = FetchType.EAGER)
    private List<Comment> replies;

    @OneToOne
    private User user;

    private Instant creationDate;

    private String body;

    public ParentComment(User user, Instant creationDate, String body) {
        super(user, creationDate, body);
        replies = new ArrayList<>();
    }

    public void addReply(Comment comment) {
        replies.add(comment);
    }
}