package com.scottlogic.training.auth;

import java.util.UUID;

public class TokenDTO {
    public boolean isValidToken;
    public String token;

    public TokenDTO(boolean isValidToken) {
        this.isValidToken = isValidToken;
        if (isValidToken) {
            this.token = UUID.randomUUID().toString();
        }
    }
}
