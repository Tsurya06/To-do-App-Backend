package com.login.todo.controllers;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.login.todo.modal.User;
import com.login.todo.services.UserService;

import jakarta.validation.Valid;

@RestController
public class UserController {
    @Autowired
    private UserService userService; 

    @PostMapping("/signup")
    public ResponseEntity<Map<String, Object>> createUser(@RequestBody User user){
        try{
            if(userService.isUserExists(user.getId())) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "User already exists!");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            
            userService.signUp(user);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Login successful!");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch(RuntimeException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } 
    }
    
    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@Valid @RequestBody User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Invalid input!");
            return ResponseEntity.badRequest().body(response);
        }
        try {
            userService.signUp(user);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "User registered successfully!");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String email, @RequestParam String password) {
        try {
            User user = userService.login(email, password);
            String successMessage= "Welcome Back "+ user.getUsername()+ " !";
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", successMessage);
            // Construct the URL to redirect to after login
            URI uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/api/v1/todo/get-todos") // Assuming this is the endpoint you want to redirect to
                    .build()
                    .toUri();

            // Add the redirect URL to the response
            response.put("redirectUrl", uri.toString());
            return ResponseEntity.ok(response); 
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
