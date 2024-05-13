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
        // if (userRepository.findByUsername(user.getUsername()) != null) {
        //     throw new RuntimeException("Username already exists");
        // }
        System.out.println("yahan hun main signup me ###################################################");
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new RuntimeException("Email already exists");
        }
        return userRepository.save(user);
    }
    public User login(String email, String password) {
        System.out.println("yahan hun main login me###################################################");
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        if (!user.getPassword().equals(password)) {
            throw new RuntimeException("Incorrect password");
        }
        return user;
    }
   
}
