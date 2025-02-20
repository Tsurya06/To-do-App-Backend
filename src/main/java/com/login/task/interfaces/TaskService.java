package com.login.task.interfaces;

import java.util.List;
import java.util.Optional;

import com.login.task.modal.Project;
import com.login.task.modal.Task;
import com.login.task.modal.User;

public interface TaskService {
    List<Task> getTasksByUserWithPagination(User user, int offset, int limit, String date);
    List<Task> getAllTasksByUser(User user);
    List<Task> getAllTasks();
    Optional<Task> getTaskById(Long id);
    Task createTask(Task task, Project project, User creator);
    Task updateTask(Long id, Task task);
    boolean deleteTask(Long id);
    Task updateTaskStatus(Long id, Task.TaskStatus status);
    // Task createTask(Task task);
    Task updateTaskPriority(Long id, Task.Priority priority);
}
