package com.login.task.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
            .name(project.getName())
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
    public ResponseEntity<Project> createProject(@RequestBody Project project, @AuthenticationPrincipal User user){
        Project createdProject = projectService.createProject(project, user);
        return ResponseEntity.ok(createdProject);
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