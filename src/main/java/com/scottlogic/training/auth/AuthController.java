package com.scottlogic.training.auth;

import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class AuthController {
    private Set<LoginCredential> logins = Set.of(
            new LoginCredential("testUsername", "testPassword")
    );

    private LoginCredential getLoginCredential(AuthDTO authDTO) {
        for (LoginCredential login : logins) {
            if (login.username.equals(authDTO.username)
                    && login.password.equals((authDTO.password))) {
                return login;
            }
        }
        return null;
    }

    @PostMapping("/auth")
    public ResponseEntity<String> postOrder(@RequestBody @Valid AuthDTO authDTO) {
        LoginCredential loginCredential = getLoginCredential(authDTO);
        if (loginCredential != null) {
            return new ResponseEntity<>(loginCredential.token, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
}
