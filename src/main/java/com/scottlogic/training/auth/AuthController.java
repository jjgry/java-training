package com.scottlogic.training.auth;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.scottlogic.training.user.User;
import com.scottlogic.training.user.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.SecretKey;
import javax.validation.Valid;

import static com.scottlogic.training.auth.PasswordService.isExpectedPassword;

@RestController
public class AuthController {
    public static final SecretKey KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    @Autowired
    UserService userService;

    private String getJWTToken(String username) {
        List<GrantedAuthority> grantedAuthorities = AuthorityUtils
                .commaSeparatedStringToAuthorityList("ROLE_USER");
        String token = Jwts
                .builder()
                .setSubject(username)
                .claim("authorities",
                        grantedAuthorities.stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList()))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 600000))
                .signWith(KEY)
                .compact();
        return "Bearer " + token;
    }

    private boolean isValidUsernamePasswordPair(AuthDTO authDTO) {
        List<User> allUsers = userService.getAllUser();
        for (User user : allUsers) {
            if (!user.getUsername().equals(authDTO.username)) {
                continue;
            }
            char[] passwordToTest = authDTO.password.toCharArray();
            byte[] expectedHash = user.getPasswordHash();
            byte[] salt = user.getSalt();
            if (isExpectedPassword(passwordToTest, salt, expectedHash)) {
                return true;
            }
        }
        return false;
    }

    @PostMapping("/auth")
    public ResponseEntity<String> postOrder(@RequestBody @Valid AuthDTO authDTO) {
        if (isValidUsernamePasswordPair(authDTO)) {
            String token = getJWTToken(authDTO.username);
            return new ResponseEntity<>(token, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
}
