package com.scottlogic.training.user;

import com.scottlogic.training.auth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    AuthService authService;

    private List<UserDTO> toUserDTOList(List<User> users) {
        return users.stream().map(User::toUserDTO).collect(Collectors.toList());
    }

    @GetMapping("/generateUsers")
    private List<UserDTO> generateUsers(@RequestParam("number") int numberOfUsers) {
        return toUserDTOList(userService.generateUsers(numberOfUsers));
    }

    @GetMapping("/user")
    private List<UserDTO> getAllUser() {
        return toUserDTOList(userService.getAllUser());
    }

    @GetMapping("/user/{username}")
    private UserDTO getUser(@PathVariable("username") String username) {
        return userService.getUserByUsername(username).toUserDTO();
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
    private UserDTO saveUser(@RequestBody NewUserDTO userDto) {
        User user = new User(userDto.getUsername(), userDto.getPassword());
        return userService.saveOrUpdate(user).toUserDTO();
    }
}
