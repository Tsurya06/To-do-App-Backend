package com.login.todo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.login.todo.modal.User;
import com.login.todo.repository.UserRepository;


@Service
public class UserService {
    @Autowired
    private UserRepository userRepository; 

    public boolean isUserExists(Long user_id) {
        return userRepository.existsById(user_id);
    }
    public User signUp(User user) {
        if (userRepository.findByUsername(user.getUsername()) != null) {
            throw new RuntimeException("User already exists");
        }
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new RuntimeException("Email already exists");
        }
        return userRepository.save(user);
    }
    public User login(String email, String password) {
        User user = userRepository.findByEmail(email);
        return user;
    }
   
}
