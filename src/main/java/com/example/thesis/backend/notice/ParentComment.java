package com.example.thesis.backend.notice;

import com.example.thesis.backend.security.auth.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class ParentComment extends Comment {

    @OneToMany(fetch = FetchType.EAGER)
    @org.hibernate.annotations.Fetch(FetchMode.SELECT)      //eliminates cartesian product loading - might be better to create dto or load in transaction
    @JoinTable(
            name = "comment_replies",
            joinColumns = @JoinColumn(
                    name = "parent_comment_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "reply_id", referencedColumnName = "id"))
    private List<Comment> replies;

    public ParentComment(User user, Instant creationDate, String body) {
        super(user, creationDate, body);
        replies = new ArrayList<>();
    }

    public void addReply(Comment comment) {
        replies.add(comment);
    }
}