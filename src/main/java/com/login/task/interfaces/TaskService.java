package com.login.task.interfaces;

import java.util.List;
import java.util.Optional;

import com.login.task.modal.Project;
import com.login.task.modal.Task;
import com.login.task.modal.User;

public interface TaskService {
    List<Task> getTasksByUserWithPagination(User user, int offset, int limit, String dueDate);
    List<Task> getAllTasksByUser(User user);
    List<Task> getAllTasks();
    Optional<Task> getTaskById(String id);
    Task createTask(Task task, Project project, User creator);
    Task updateTask(String id, Task task);
    boolean deleteTask(String id);
    Task updateTaskStatus(String id, Task.TaskStatus status);
    // Task createTask(Task task);
    Task updateTaskPriority(String id, Task.Priority priority);
}
