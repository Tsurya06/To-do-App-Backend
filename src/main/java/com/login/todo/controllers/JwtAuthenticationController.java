package com.login.todo.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.login.todo.jwt.JwtHelper;
import com.login.todo.modal.JwtRequest;
import com.login.todo.modal.JwtResponse;
import com.login.todo.modal.User;
import com.login.todo.services.UserService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/auth")
@Slf4j
public class JwtAuthenticationController {

    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private AuthenticationManager manager;
    @Autowired
    private JwtHelper helper;
    @Autowired
    private UserService userService;
    @Autowired
    PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody JwtRequest request) {
        Map<String, Object> response = new HashMap<>();
        try {
            // authenticate
            this.doAuthenticate(request.getEmail(), request.getPassword());

            UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
            String token = this.helper.generateToken(userDetails);
            
            String email = userDetails.getUsername();
            String name = email.split("@")[0];

            response.put("success", true);
            response.put("message", "Login successful");
            response.put("access", token);
            response.put("refresh", token); // replace with actual refresh token
            
            Map<String, String> user = new HashMap<>();
            
            user.put("id", userDetails.getUsername()); // replace with actual user id
            user.put("username", name);
            user.put("email", request.getEmail());
            response.put("user", user);
        } catch (UsernameNotFoundException e) {
            response.put("success", false);
            response.put("message", "User does not exist");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/signup")
    public ResponseEntity<Map<String, Object>> Signup(@RequestBody User user) {
        log.info("User" + user.toString());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Map<String, Object> response = new HashMap<>();
        try {
            userService.signUp(user);
            response.put("success", true);
            response.put("message", "User Created Successfully");
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "User Already Exists");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private void doAuthenticate(String username, String password) {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username,
                password);
        try {
            manager.authenticate(authentication);
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Credentials Invalid !!");
        }

    }
}
