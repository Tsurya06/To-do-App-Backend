package com.login.task.controllers;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.login.task.jwt.JwtHelper;
import com.login.task.modal.JwtRequest;
import com.login.task.modal.RefreshTokenRequest;
import com.login.task.modal.User;
import com.login.task.repository.UserRepository;
import com.login.task.services.RefreshTokenServiceImpl;
import com.login.task.services.UserServiceImpl;
import com.login.task.modal.RefreshToken;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/auth")
@Slf4j
public class JwtAuthenticationController {

    @Autowired
    UserRepository userRepository;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private AuthenticationManager manager;
    @Autowired
    private JwtHelper helper;
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    RefreshTokenServiceImpl refreshTokenService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody JwtRequest request) {
        Map<String, Object> response = new HashMap<>();
        try {
            // authenticate
            this.doAuthenticate(request.getEmail(), request.getPassword());

            UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
            User userEntity = userRepository.findByEmail(userDetails.getUsername());
            String jwtToken = this.helper.generateToken(userEntity);
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getUsername());
            
            response.put("success", true);
            response.put("message", "Welcome back "+ userEntity.getUsername().toUpperCase()+" !");
            response.put("access", jwtToken);
            response.put("refresh", refreshToken.getRefreshToken()); 
            
            Map<String, Object> user = new HashMap<>();
            
            user.put("id", userEntity.getId()); 
            user.put("username", userEntity.getUsername());
            user.put("email", request.getEmail());
            response.put("user", user);
        } catch (UsernameNotFoundException e) {
            response.put("success", false);
            response.put("message", "User  does not exist.");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/signup")
    public ResponseEntity<Map<String, Object>> Signup(@RequestBody User user) {
        log.info("User#######" + user.getUsername());
        Map<String, Object> response = new HashMap<>();
        try {
            userService.signUp(user);
            response.put("success", true);
            response.put("message", "Signup Successful "+ user.getUsername().toUpperCase()+", Please Login to continue.");
        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("message", "User "+user.getUsername().toUpperCase()+" Already Exists");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout(@RequestBody RefreshTokenRequest request) {
        Map<String, Object> response = new HashMap<>();
        try {
            // Validate the refresh token exists
            if (request.getRefreshToken() != null && !request.getRefreshToken().isEmpty()) {
                // Remove/invalidate the refresh token
                refreshTokenService.revokeRefreshToken(request.getRefreshToken());
                
                response.put("success", true);
                response.put("message", "Logout successful. All tokens have been invalidated.");
            } else {
                response.put("success", false);
                response.put("message", "Refresh token is required to complete logout");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Logout failed: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/admin/users")
    public ResponseEntity<Map<String, Object>> addUser(@RequestBody User user) {
        log.info("Admin adding User#######" + user.getUsername());
        Map<String, Object> response = new HashMap<>();
        try {
            userService.signUp(user);
            response.put("success", true);
            response.put("message", "User "+ user.getUsername().toUpperCase()+" added successfully.");
        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("message", "User "+user.getUsername().toUpperCase()+" Already Exists");
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
    @PostMapping("/refresh")
    public ResponseEntity<Map<String, Object>> refresh(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        Map<String, Object> response = new HashMap<>();
        try {
            // Verify and get the refresh token
            RefreshToken refreshTokenObj = refreshTokenService.verifyRefreshToken(refreshTokenRequest.getRefreshToken());
            User user = refreshTokenObj.getUser();
            
            // Generate a new JWT token
            String jwtToken = this.helper.generateToken(user);
            
            // Optionally create a new refresh token (sliding expiration)
            RefreshToken newRefreshToken = refreshTokenService.rotateRefreshToken(refreshTokenObj);
            
            response.put("success", true);
            response.put("message", "Token Refreshed Successfully!");
            response.put("access", jwtToken);
            response.put("refresh", newRefreshToken.getRefreshToken());
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
