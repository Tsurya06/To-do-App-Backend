package com.login.task.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.login.task.modal.Project;
import com.login.task.modal.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {
    
    @GetMapping
    public ResponseEntity<List<Project>> getAllProjects(@AuthenticationPrincipal User user){
        return null;
    }
    
    @PostMapping
    public ResponseEntity<Project> createProject(@RequestBody Project project){
        return null;
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Project> getProjectById(@PathVariable String id){
        return null;
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Project> updateProject(@PathVariable String id, @RequestBody Project project){
        return null;
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProject(@PathVariable String id){
        return null;
    }
} 