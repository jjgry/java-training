package com.scottlogic.training.auth;

import java.util.UUID;

public class LoginCredential {
    public String username;
    public String password;
    public String token;

    public LoginCredential(String username, String password) {
        this.username = username;
        this.password = password;
        this.token = UUID.randomUUID().toString();
    }
}
