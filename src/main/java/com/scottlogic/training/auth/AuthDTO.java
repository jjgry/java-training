package com.scottlogic.training.auth;

import javax.validation.constraints.NotNull;


public class AuthDTO {
    @NotNull
    public String username;
    @NotNull
    public String password;

    public AuthDTO() {}

    public AuthDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public String toString() {
        return "AuthDTO{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
