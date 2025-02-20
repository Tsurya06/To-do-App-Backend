package com.login.task.interfaces;

import com.login.task.modal.Project;
import com.login.task.modal.User;

import java.util.List;

public interface ProjectService {
    Project createProject(Project project, User owner);
    List<Project> getUserProjects(User user);
    Project updateProject(Long id, Project project);
    void deleteProject(Long id);
}
