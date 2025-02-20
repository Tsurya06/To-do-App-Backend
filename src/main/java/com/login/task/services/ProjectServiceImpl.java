package com.login.task.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.login.task.interfaces.ProjectService;
import com.login.task.modal.Project;
import com.login.task.modal.User;
import com.login.task.repository.ProjectRepository;

import java.util.List;

@Service
public class ProjectServiceImpl implements ProjectService {
    
    @Autowired
    private ProjectRepository projectRepository;
    
    @Override
    public Project createProject(Project project, User owner) {
        project.setOwner(owner);
        return projectRepository.save(project);
    }
    
    @Override
    public List<Project> getUserProjects(User user) {
        return projectRepository.findProjectByOwner(user);
    }
    
    @Override
    public Project updateProject(Long id, Project project) {
        Project existingProject = projectRepository.findById(id)
          .orElseThrow(() -> new RuntimeException("Project not found"));
        return projectRepository.save(existingProject);
    }

    @Override
    public void deleteProject(Long id) {
        projectRepository.deleteById(id);
    }
} 