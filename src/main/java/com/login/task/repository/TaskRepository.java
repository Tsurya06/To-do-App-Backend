package com.login.task.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.login.task.modal.Task;
import com.login.task.modal.User;
import com.login.task.modal.Project;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, String>{
    Page<Task> findByUserAndDueDate(User user, LocalDate dueDate, Pageable pageable);
    Page<Task> findByUser(User user, Pageable pageable);
    List<Task> findByUser(User user);
    List<Task> findByProject(Project project);
    List<Task> findByProjectAndStatus(Project project, Task.TaskStatus status);
}
    