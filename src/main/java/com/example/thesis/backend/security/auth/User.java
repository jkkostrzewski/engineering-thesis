package com.example.thesis.backend.security.auth;

import com.example.thesis.backend.floor.Floor;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static javax.persistence.GenerationType.AUTO;

@Entity
@Getter
@Setter
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
    private List<Role> roles = new ArrayList<>();           //change to One Role only?

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", roles=" + roles +
                ", floors=" + floors +
                '}';
    }
}