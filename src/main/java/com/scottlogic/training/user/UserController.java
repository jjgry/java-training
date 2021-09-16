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

    //creating a get mapping that retrieves all the students detail from the database
    @GetMapping("/user")
    private List<User> getAllUser() {
        return userService.getAllUser();
    }

    //creating a get mapping that retrieves the detail of a specific student
    @GetMapping("/user/{username}")
    private User getUser(@PathVariable("username") String username) {
        return userService.getUserByUsername(username);
    }

    //creating a delete mapping that deletes a specific student
    @DeleteMapping("/user/{username}")
    private void deleteUser(@PathVariable("username") String username) {
        userService.delete(username);
    }

    //creating post mapping that post the student detail in the database
    @PostMapping("/user")
    private User saveUser(@RequestBody User user) {
        userService.saveOrUpdate(user);
        return user;
    }
}
