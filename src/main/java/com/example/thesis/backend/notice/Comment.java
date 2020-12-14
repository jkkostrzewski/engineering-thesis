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

    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "comment_replies",
            joinColumns = @JoinColumn(
                    name = "comment_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "reply_comment_id", referencedColumnName = "id"))
    private List<Comment> replies;

    private boolean parentComment;

    @OneToOne
    private User user;

    private Instant creationDate;

    private String body;

    public Comment(boolean parentComment, User user, Instant creationDate, String body) {
        this.parentComment = parentComment;
        if (parentComment) {
            replies = new ArrayList<>();
        }
        this.user = user;
        this.creationDate = creationDate;
        this.body = body;
    }

    public void addReply(Comment comment) {
        if(parentComment) {
            replies.add(comment);
        } else {
            throw new RuntimeException();
        }
    }
}
