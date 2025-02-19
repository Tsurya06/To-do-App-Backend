package com.login.task.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.login.task.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService{

    @Autowired
    private UserRepository userRepository; 

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        if (userRepository.findByEmail(email) == null) {
            throw new RuntimeException("Email already exists");
        }
        return userRepository.findByEmail(email);
    }
    
}
