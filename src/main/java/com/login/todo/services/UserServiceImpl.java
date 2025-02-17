package com.login.todo.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.login.todo.interfaces.UserService;
import com.login.todo.modal.User;
import com.login.todo.repository.UserRepository;


@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserRepository userRepository; 
    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public boolean isUserExists(Long user_id) {
        return userRepository.existsById(user_id);
    }
    @Override
    public User signUp(User user) {
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new RuntimeException("Email already exists");
        }
        // user.setUser_id(UUID.randomUUID().toString());
        user.setName(user.getName());
        user.setEmail(user.getUsername());
        user.setPassword(passwordEncoder.encode( user.getPassword()));
        return userRepository.save(user);
    }
    @Override
    public User login(String email, String password) {
        User user = userRepository.findByEmail(email);
        return user;
    }
   
}
