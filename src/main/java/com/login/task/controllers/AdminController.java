package com.login.task.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.login.task.repository.UserRepository;
import com.login.task.modal.User;
import com.login.task.dto.UserDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    private UserDTO convertToDTO(User user) {
        return UserDTO.builder()
            .id(user.getId())
            .name(user.getUsername())
            .email(user.getEmail())
            .createdAt(user.getCreatedAt() != null ? 
                user.getCreatedAt().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")) : null)
            .taskCount(user.getTasks() != null ? user.getTasks().size() : 0)
            .projectCount(user.getProjects() != null ? user.getProjects().size() : 0)
            .build();
    }

    @GetMapping("/users")
    public ResponseEntity<Map<String, Object>> getAllUsers(
            @RequestParam(required = false) String search) {
        try {
            List<User> users;
            if (search != null && !search.trim().isEmpty()) {
                users = userRepository.findByUsernameContainingOrEmailContaining(search.toLowerCase(), search.toLowerCase());
            } else {
                users = userRepository.findAll();
            }

            // Convert users to DTOs
            List<UserDTO> userDTOs = users.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", userDTOs);
            response.put("message", "Users fetched successfully.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error fetching users: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
    @GetMapping("/users/{id}")
    public ResponseEntity<Map<String, Object>> getUserById(
            @PathVariable Long id) {
        try {
            User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
            UserDTO userDTO = convertToDTO(user);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", userDTO);
            response.put("message", "User fetched successfully.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error fetching users: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
    @PatchMapping("/users/{id}")
    public ResponseEntity<Map<String, Object>> updateUserById(
            @PathVariable Long id,
            @RequestBody UserDTO userDTO) {
        try {
            User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
            user.setUsername(userDTO.getName());
            user.setEmail(userDTO.getEmail());
            userRepository.save(user);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "User updated successfully.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error fetching users: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Map<String, Object>> deleteUserById(
            @PathVariable Long id) {
        try {
            User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
            UserDTO userDTO = convertToDTO(user);
            userRepository.delete(user);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", userDTO.getName() + " deleted successfully.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error fetching users: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
} 