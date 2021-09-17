package com.scottlogic.training.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {
    @Autowired
    UserService userService;

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
    private void deleteUser(@PathVariable("username") String username) {
        userService.delete(username);
    }

    @PostMapping("/user")
    private User saveUser(@RequestBody User user) {
        userService.saveOrUpdate(user);
        return user;
    }
}
