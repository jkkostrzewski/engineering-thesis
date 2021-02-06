package com.example.thesis.backend.notice;

import com.example.thesis.backend.floor.Floor;
import com.example.thesis.backend.security.auth.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "board_permission_users",
            joinColumns = @JoinColumn(
                    name = "board_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"))
    private Set<User> permissionUsers = new HashSet<>();

    public NoticeBoard(@Size(max = 30) String name, Floor owner) {
        this.name = name;
        this.owner = owner;
    }

    public void addNotice(Notice notice) {
        notices.add(0, notice);
    }

    public void addPermissionUser(User user) {
        permissionUsers.add(user);
    }

    public boolean isEligibleToEdit(String username) {
        return permissionUsers.stream().anyMatch(permissionUser ->
                permissionUser.getUsername().equals(username));
    }
}


