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

@Service
public class TaskServiceImpl implements TaskService {
    @Autowired
    private TaskRepository TaskRepository;
    
    @Override
    public List<Task> getTasksByUserWithPagination(User user, int offset, int limit, String date) {
        Pageable pageable = PageRequest.of(offset - 1, limit);
        Page<Task> taskPage;
        
        if (date != null && !date.trim().isEmpty()) {
            // Parse the date string to LocalDate
            LocalDate parsedDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            taskPage = TaskRepository.findByUserAndDate(user, parsedDate, pageable);
        } else {
            taskPage = TaskRepository.findByUser(user, pageable);
        }
        
        return taskPage.getContent();
    }
    @Override
    public List<Task> getAllTasksByUser(User user) {
        return TaskRepository.findByUser(user);
    }
    @Override
    public List<Task> getAllTasks() {
        return TaskRepository.findAll();
    }
    @Override
    public Optional<Task> getTaskById(Long id) {
        return TaskRepository.findById(id);
    }
    @Override
    public Task createTask(Task Task) {
        return TaskRepository.save(Task);
    }
    @Override
    public Task updateTask(Long id, Task Task) {
        if (TaskRepository.existsById(id)) {
            Task.setId(id);
            return TaskRepository.save(Task);
        }
        return null;
    }
    @Override
    public boolean deleteTask(Long id) {
        if (TaskRepository.existsById(id)) {
            TaskRepository.deleteById(id);
            return true;
        }
        return false;
    }
}

