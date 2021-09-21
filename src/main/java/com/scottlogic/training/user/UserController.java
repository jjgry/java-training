package com.scottlogic.training.user;

import com.scottlogic.training.auth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    AuthService authService;

    @GetMapping("/generateUsers")
    private List<User> generateUsers(@RequestParam("number") int numberOfUsers) {
        return userService.generateUsers(numberOfUsers);
    }

    @GetMapping("/user")
    private List<User> getAllUser() {
        return userService.getAllUser();
    }

    @GetMapping("/user/{username}")
    private User getUser(@PathVariable("username") String username) {
        return userService.getUserByUsername(username);
    }

    @DeleteMapping("/user/{username}")
    private ResponseEntity<String> deleteUser(
            @RequestHeader(value = "Authorization") String authorisation,
            @PathVariable("username") String username) {
        String authUsername = authService.getUsername(authorisation);
        if (!authUsername.equals(username)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        userService.delete(username);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/user")
    private User saveUser(@RequestBody UserDTO userDto) {
        User user = new User(userDto.getUsername(), userDto.getPassword());
        return userService.saveOrUpdate(user);
    }
}
