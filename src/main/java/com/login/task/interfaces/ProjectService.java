package com.login.task.interfaces;

import com.login.task.modal.Project;
import com.login.task.modal.User;

import java.util.List;
import java.util.UUID;

public interface ProjectService {
    Project createProject(Project project, User owner);
    List<Project> getUserProjects(User user);
    Project updateProject(UUID id, Project project);
    void deleteProject(UUID id);
}
