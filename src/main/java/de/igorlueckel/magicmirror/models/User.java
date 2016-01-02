package de.igorlueckel.magicmirror.models;

import de.igorlueckel.magicmirror.enums.Role;

import javax.persistence.*;

/**
 * Created by Igor on 20.09.2015.
 */
@Entity
public class User {
    @Id
    @GeneratedValue
    Integer id;
    @Column(nullable = false)
    String username;
    @Column(nullable = false)
    String password;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    Role role;

    public Integer getId() {
        return id;
    }

    public User setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public User setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    public Role getRole() {
        return role;
    }

    public User setRole(Role role) {
        this.role = role;
        return this;
    }
}
