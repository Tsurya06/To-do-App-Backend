package com.login.task.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

import com.login.task.modal.Project;
import com.login.task.modal.User;

@Repository
public interface ProjectRepository extends JpaRepository<Project, String> {
    List<Project> findProjectByUser(User user);
    Project findProjectById(String id);
    // List<Project> findProjectByOwnerOrderByCreatedAtDesc(User owner);
} 