package com.login.task.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.login.task.interfaces.ProjectService;
import com.login.task.modal.Project;
import com.login.task.modal.User;
import com.login.task.repository.ProjectRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectServiceImpl implements ProjectService {
    
    @Autowired
    private ProjectRepository projectRepository;
    
    @Override
    public Project createProject(Project project, User user) {
        project.setUser(user);
        return projectRepository.save(project);
    }
    
    @Override
    public List<Project> getUserProjects(User user) {
        return projectRepository.findProjectByUser(user);
    }
    
    @Override
    public Project updateProject(String id, Project project) {
        Project existingProject = projectRepository.findById(id)
          .orElseThrow(() -> new RuntimeException("Project not found"));
        return projectRepository.save(existingProject);
    }

    @Override
    public void deleteProject(String id) {
        projectRepository.deleteById(id);
    }

    @Override
    public Optional<Project> getProjectById(String id) {
        return projectRepository.findById(id);
    }
} 