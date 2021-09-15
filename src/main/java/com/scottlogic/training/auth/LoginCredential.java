package com.scottlogic.training.auth;

import java.util.Objects;

public class LoginCredential {
    public String username;
    public String password;

    public LoginCredential(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoginCredential that = (LoginCredential) o;
        return Objects.equals(username, that.username) && Objects.equals(password, that.password);
    }
}
