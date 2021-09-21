package com.scottlogic.training.user;

import javax.persistence.*;

import java.util.HashMap;
import java.util.Map;

import static com.scottlogic.training.auth.PasswordService.getNextSalt;
import static com.scottlogic.training.auth.PasswordService.hash;

@Entity
public class User {
    @Id
    private String username;
    private byte[] salt;
    private byte[] passwordHash;

    public User() {}

    public User(String username, String password) {
        this.username = username;
        this.salt = getNextSalt();
        this.passwordHash = hash(password.toCharArray(), salt);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public byte[] getPasswordHash() {
        return passwordHash;
    }

    public byte[] getSalt() {
        return salt;
    }

    public UserDTO toUserDTO() {
        return new UserDTO(username);
    }

}