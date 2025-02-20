package com.login.task.services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.login.task.interfaces.TaskService;
import com.login.task.modal.Task;
import com.login.task.modal.User;
import com.login.task.repository.TaskRepository;

import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import com.login.task.modal.Project;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;


@Service
public class TaskServiceImpl implements TaskService {
    @Autowired
    private TaskRepository taskRepository;
    
    @Override
    public List<Task> getTasksByUserWithPagination(User user, int offset, int limit, String dueDate) {
        Pageable pageable = PageRequest.of(offset - 1, limit);
        Page<Task> taskPage;
        if (dueDate != null && !dueDate.trim().isEmpty()) {
            try {
                LocalDate parsedDate = LocalDate.parse(dueDate, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                taskPage = taskRepository.findByUserAndDueDate(user, parsedDate, pageable);
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("Invalid date format. Expected format: dd-MM-yyyy", e);
            }
        } else {
            taskPage = taskRepository.findByUser(user, pageable);
        }
        return taskPage.getContent();
    }
    @Override
    public List<Task> getAllTasksByUser(User user) {
        return taskRepository.findByUser(user);
    }
    @Override
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }
    @Override
    public Optional<Task> getTaskById(String id) {
        return taskRepository.findById(id);
    }
    @Override
    public Task createTask(Task task, Project project, User creator) {
        // if (project != null) {
        //     task.setProject(project);
        // }
        task.setUser(creator);
        task.setCreatedAt(LocalDateTime.now());
        task.setUpdatedAt(LocalDateTime.now());
        
        // Set default values if not provided
        if (task.getStatus() == null) {
            task.setStatus(Task.TaskStatus.TO_DO);
        }
        if (task.getPriority() == null) {
            task.setPriority(Task.Priority.MEDIUM);
        }
        
        return taskRepository.save(task);
    }
    @Override
    public Task updateTask(String id, Task task) {
        if (taskRepository.existsById(id)) {
            task.setId(id);
            return taskRepository.save(task);
        }
        return null;
    }
    @Override
    public boolean deleteTask(String id) {
        if (taskRepository.existsById(id)) {
            taskRepository.deleteById(id);
            return true;
        }
        return false;
    }
    @Override
    public Task updateTaskStatus(String id, Task.TaskStatus status) {
        Task task = taskRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Task not found"));
        task.setStatus(status);
        return taskRepository.save(task);
    }

    @Override
    public Task updateTaskPriority(String id, Task.Priority priority) {
        Task task = taskRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Task not found"));
        task.setPriority(priority);
        return taskRepository.save(task);
    }
}

