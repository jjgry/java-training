package com.scottlogic.training.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public List<User> generateUsers(int numberOfUsers) {
        List<User> generatedUsers = new ArrayList<>();
        for (int i = 1; i <= numberOfUsers; i++) {
            String username = "username" + i;
            String password = "password" + i;
            generatedUsers.add(new User(username, password));
        }
        for (User user : generatedUsers) {
            saveOrUpdate(user);
        }
        return generatedUsers;
    }

    public List<User> getAllUser() {
        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);
        return users;
    }

    public User getUserByUsername(String username) {
        Optional<User> user = userRepository.findById(username);
        return user.orElse(null);
    }

    public User saveOrUpdate(User user) {
        return userRepository.save(user);
    }

    public void delete(String username) {
        userRepository.deleteById(username);
    }
}
