package com.example.thesis.backend.notice;

import lombok.*;

import javax.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;

import static javax.persistence.FetchType.EAGER;
import static javax.persistence.GenerationType.AUTO;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Notice {

    @Id
    @GeneratedValue(strategy = AUTO)
    private long id;

    private Instant creationDate;
    private String title;

    @Column(length = 10000)
    private String body;

    @Lob
    private byte[] image;
//    private String[] tags;

    @OneToMany(fetch = EAGER, cascade = CascadeType.ALL)
    @OrderBy(value = "id")
    @JoinTable(
            name = "notice_parent_comments",
            joinColumns = @JoinColumn(
                    name = "notice_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "parent_comment_id", referencedColumnName = "id"))
    private Collection<ParentComment> parentComments = new ArrayList<>();

    public void addParentComment(ParentComment comment) {
        parentComments.add(comment);
    }

    public void addReply(ParentComment parent, Comment comment) {
        parentComments.stream()
                .filter(element -> element.equals(parent))
                .findFirst()
                .orElseThrow(RuntimeException::new)
                .addReply(comment);
//                .forEach((parentComment) -> parentComment.addReply(comment));
    }
}

