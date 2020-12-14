package com.example.thesis.backend.notice;

import com.example.thesis.backend.floor.Floor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;

import static javax.persistence.CascadeType.ALL;
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
    private byte[] image;       //do oddzielnej tabeli moze?
//    private String[] tags;

    @OneToMany(fetch = EAGER, cascade = ALL)
    private Collection<Comment> comments = new ArrayList<>();

    public void addComment(Comment comment) {
        comments.add(comment);
    }
}

