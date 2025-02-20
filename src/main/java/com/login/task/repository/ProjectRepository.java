package com.login.task.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

import com.login.task.modal.Project;
import com.login.task.modal.User;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findProjectByOwner(User owner);
    Project findProjectById(Long id);
    // List<Project> findProjectByOwnerOrderByCreatedAtDesc(User owner);
} 