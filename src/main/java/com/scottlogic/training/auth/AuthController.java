package com.scottlogic.training.auth;

import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class AuthController {
    private Map<LoginCredential, TokenDTO> logins = Map.of(
            new LoginCredential("testUsername", "testPassword"),
            new TokenDTO(true));

    private LoginCredential getLoginCredential(AuthDTO authDTO) {
        LoginCredential loginAttempt = new LoginCredential(authDTO.username, authDTO.password);
        for (LoginCredential login : logins.keySet()) {
            if (login.equals(loginAttempt)) {
                return login;
            }
        }
        return null;
    }

    @PostMapping("/auth")
    public TokenDTO postOrder(@RequestBody @Valid AuthDTO authDTO) {
        LoginCredential loginCredential = getLoginCredential(authDTO);
        if (loginCredential != null) {
            return logins.get(loginCredential);
        }
        return new TokenDTO(false);
    }
}
