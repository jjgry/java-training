package com.scottlogic.training.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class AuthController {
    @Autowired
    AuthService authService;

    @PostMapping("/auth")
    public ResponseEntity<String> postOrder(@RequestBody @Valid AuthDTO authDTO) {
        if (authService.isValidUsernamePasswordPair(authDTO)) {
            String token = authService.getJWTToken(authDTO.username);
            return new ResponseEntity<>(token, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
}
