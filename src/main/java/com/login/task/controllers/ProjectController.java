package com.login.task.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.login.task.dto.ProjectDto;
import com.login.task.modal.Project;
import com.login.task.modal.User;
import com.login.task.repository.ProjectRepository;
import com.login.task.services.ProjectServiceImpl;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

@RestController
@RequestMapping("/api/v1/project")
public class ProjectController {
    @Autowired
    private ProjectServiceImpl projectService;
    @Autowired
    private ProjectRepository projectRepository;
    
    private ProjectDto convertToDTO(Project project) {
        return ProjectDto.builder()
            .id(project.getId())
            .title(project.getTitle())
            .description(project.getDescription())
            .user(project.getUser())
            .tasks(project.getTasks())
            .createdAt(project.getCreatedAt())
            .updatedAt(project.getUpdatedAt())
            .build();
    }

    @GetMapping("/all")
    public ResponseEntity<List<Project>> getAllProjects(@AuthenticationPrincipal User user){
        return null;
    }
    
    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createProject(@RequestBody Project project, @AuthenticationPrincipal User user){
        Project createdProject = projectService.createProject(project, user);
        try {
            HashMap<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Project created successfully");
            response.put("data", createdProject);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            HashMap<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error creating project: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
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