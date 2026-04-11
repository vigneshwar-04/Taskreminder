package com.example.taskreminder.service;

import com.example.taskreminder.entity.User;
import com.example.taskreminder.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // REGISTER
    public void register(User user) {
        userRepository.save(user);
    }

    // LOGIN
    public User login(String email, String password) {

        Optional<User> optionalUser = userRepository.findByEmail(email);

        if(optionalUser.isPresent()) {
            User user = optionalUser.get();

            if(user.getPassword().equals(password)) {
                return user;
            }
        }

        return null;
    }

    // FIND USER
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}