package com.scottlogic.training.user;

import com.scottlogic.training.user.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {
}