package com.scottlogic.training.auth;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.scottlogic.training.user.User;
import com.scottlogic.training.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class AuthController {
    private Map<String, String> usernameToToken;

    @Autowired
    UserService userService;

    public AuthController() {
        usernameToToken = new HashMap<>();
    }

    private String generateToken(AuthDTO authDTO) {
        String token = authDTO.username + authDTO.password;
        usernameToToken.put(authDTO.username, token);
        return token;
    }

    private boolean isValidUsernamePasswordPair(AuthDTO authDTO) {
        List<User> allUsers = userService.getAllUser();
        for (User user: allUsers) {
            if (user.getUsername().equals(authDTO.username)
                    && user.getPassword().equals(authDTO.password)) {
                return true;
            }
        }
        return false;
    }

    @PostMapping("/auth")
    public ResponseEntity<String> postOrder(@RequestBody @Valid AuthDTO authDTO) {
        if (isValidUsernamePasswordPair(authDTO)) {
            String token = generateToken(authDTO);
            return new ResponseEntity<>(token, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
}
