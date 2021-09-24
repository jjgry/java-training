package com.scottlogic.training.user;

import java.util.HashMap;
import java.util.Map;

import static com.scottlogic.training.auth.PasswordService.getNextSalt;
import static com.scottlogic.training.auth.PasswordService.hash;

public class User {
    public final String username;
    public final byte[] salt;
    public final byte[] passwordHash;

    public User(String username, String password) {
        this.username = username;
        this.salt = getNextSalt();
        this.passwordHash = hash(password.toCharArray(), salt);
    }

    public User(String username, byte[] salt, byte[] passwordHash) {
        this.username = username;
        this.salt = salt;
        this.passwordHash = passwordHash;
    }

    public UserDTO toUserDTO() {
        return new UserDTO(username);
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("username", username);
        map.put("salt", new String(salt));
        map.put("passwordHash", new String(passwordHash));
        return map;
    }

}