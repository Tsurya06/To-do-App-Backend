package com.login.todo.services;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.login.todo.modal.User;
import com.login.todo.repository.UserRepository;


@Service
public class UserService {
    @Autowired
    private UserRepository userRepository; 
    @Autowired
    PasswordEncoder passwordEncoder;

    public boolean isUserExists(String user_id) {
        return userRepository.existsById(user_id);
    }
    public User signUp(User user) {
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new RuntimeException("Email already exists");
        }
        user.setUser_id(UUID.randomUUID().toString());
        user.setName(user.getUsername());
        user.setPassword(passwordEncoder.encode( user.getPassword()));
        return userRepository.save(user);
    }
    public User login(String email, String password) {
        User user = userRepository.findByEmail(email);
        return user;
    }
   
}
