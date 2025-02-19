package com.login.task.interfaces;

import java.util.List;
import java.util.Optional;

import com.login.task.modal.Task;
import com.login.task.modal.User;

public interface TaskService {
    List<Task> getTasksByUserWithPagination(User user, int offset, int limit, String date);
    List<Task> getAllTasksByUser(User user);
    List<Task> getAllTasks();
    Optional<Task> getTaskById(Long id);
    Task createTask(Task Task);
    Task updateTask(Long id, Task Task);
    boolean deleteTask(Long id);
}
