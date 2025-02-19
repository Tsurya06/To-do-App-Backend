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


@Service
public class TaskServiceImpl implements TaskService {
    @Autowired
    private TaskRepository taskRepository;
    
    @Override
    public List<Task> getTasksByUserWithPagination(User user, int offset, int limit, String date) {
        Pageable pageable = PageRequest.of(offset - 1, limit);
        Page<Task> taskPage;
        
        if (date != null && !date.trim().isEmpty()) {
            // Parse the date string to LocalDate
            LocalDate parsedDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            taskPage = taskRepository.findByUserAndDate(user, parsedDate, pageable);
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
    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }
    @Override
    public Task createTask(Task task, Project project, User creator) {
        task.setProject(project);
        task.setUser(creator);
        return taskRepository.save(task);
    }
    @Override
    public Task updateTask(Long id, Task task) {
        if (taskRepository.existsById(id)) {
            task.setId(id);
            return taskRepository.save(task);
        }
        return null;
    }
    @Override
    public boolean deleteTask(Long id) {
        if (taskRepository.existsById(id)) {
            taskRepository.deleteById(id);
            return true;
        }
        return false;
    }
    @Override
    public Task updateTaskStatus(Long id, Task.TaskStatus status) {
        Task task = taskRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Task not found"));
        task.setStatus(status);
        return taskRepository.save(task);
    }
    @Override
    public Task createTask(Task task) {
        return taskRepository.save(task);
    }
    @Override
    public Task updateTaskPriority(Long id, Task.Priority priority) {
        Task task = taskRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Task not found"));
        task.setPriority(priority);
        return taskRepository.save(task);
    }
}

