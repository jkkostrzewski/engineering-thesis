package com.example.thesis.backend.security.auth;

import com.example.thesis.backend.floor.Floor;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import java.util.HashSet;
import java.util.Set;

import static javax.persistence.GenerationType.AUTO;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;

    @Size(max = 80)
    @NotBlank
    private String firstName;

    @Size(max = 160)
    @NotBlank
    private String lastName;

    @Column(unique = true)
    @Size(max = 20)
    @NotBlank
    private String username;

    @Email
    @Size(max = 50)
    @NotBlank
    private String email;

    @Size(max = 120)
    @NotBlank
    @JsonIgnore
    private String password;

    @JsonIgnore
    private boolean enabled;
    @JsonIgnore
    private boolean accountNonExpired;
    @JsonIgnore
    private boolean accountNonLocked;
    @JsonIgnore
    private boolean credentialsNonExpired;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"))
    private Set<Role> roles = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_floors",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "floor_id", referencedColumnName = "id"))
    private Set<Floor> floors = new HashSet<>();

    public User(@Size(max = 80) @NotBlank String firstName, @Size(max = 160) @NotBlank String lastName,
                @Size(max = 20) @NotBlank String username, @Email @Size(max = 50) @NotBlank String email,
                @Size(max = 120) @NotBlank String password, boolean enabled, boolean accountNonExpired,
                boolean accountNonLocked, boolean credentialsNonExpired) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.password = password;
        this.enabled = enabled;
        this.accountNonExpired = accountNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.credentialsNonExpired = credentialsNonExpired;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public void addRole(Role role) {
        roles.add(role);
    }

    public void addFloor(Floor floor) {
        floors.add(floor);
    }
}