package com.example.thesis.backend.notice;

import com.example.thesis.backend.floor.Floor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class NoticeBoard {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Size(max = 30)
    private String name;

    @OneToOne
    private Floor owner;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "board_notices",
            joinColumns = @JoinColumn(
                    name = "board_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "notice_id", referencedColumnName = "id"))
    private List<Notice> notices = new ArrayList<>();

    public NoticeBoard(@Size(max = 30) String name, Floor owner) {
        this.name = name;
        this.owner = owner;
    }

    public void addNotice(Notice notice) {
        notices.add(0, notice);
    }
}


