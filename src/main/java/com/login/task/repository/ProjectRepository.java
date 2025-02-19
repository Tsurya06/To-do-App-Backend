package com.login.task.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

import com.login.task.modal.Project;
import com.login.task.modal.User;

@Repository
public interface ProjectRepository extends JpaRepository<Project, UUID> {
    List<Project> findByOwner(User owner);
    List<Project> findByOwnerOrderByCreatedAtDesc(User owner);
} 