package com.scottlogic.training.auth;

import com.scottlogic.training.user.User;
import com.scottlogic.training.user.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.scottlogic.training.auth.PasswordService.isExpectedPassword;

@Service
public class AuthService {
    @Autowired
    UserService userService;

    public static final SecretKey KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    public String getJWTToken(String username) {
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


    public boolean isValidUsernamePasswordPair(AuthDTO authDTO) {
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
}
