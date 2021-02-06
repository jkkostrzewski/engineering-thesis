package com.example.thesis.backend.notice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
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

    private String createdByUsername;

    private boolean active;

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
    }

    public void deactivate() {
        this.active = false;
    }
}

