package com.login.task.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.login.task.exception.TaskException;
import com.login.task.modal.Task;
import com.login.task.modal.User;
import com.login.task.services.TaskServiceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/task")
public class TaskController {
    @Autowired
    private TaskServiceImpl taskService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> createSimpleTask(
        @RequestBody Task task,
        @AuthenticationPrincipal User currentUser
    ) {
        try {
            if (task.getTitle() == null || task.getTitle().trim().isEmpty()) {
                throw new TaskException("Task title is required", HttpStatus.BAD_REQUEST);
            }

           
            if (task.getAssignee() == null) {
                task.setAssignee(currentUser);
            }

          
            if (task.getDate() != null) {
                LocalDateTime dateTime = task.getDate().toLocalDate().atStartOfDay();
                task.setDate(dateTime);
            }

           
            if (task.getDate() != null) {
                LocalDateTime dueDateTime = task.getDate().toLocalDate().atTime(23, 59, 59);
                task.setDate(dueDateTime);
            }
            
            Task createdTask = taskService.createTask(task, task.getProject(), task.getAssignee());
            return createSuccessResponse("Task created successfully", createdTask);
        } catch (TaskException e) {
            return createErrorResponse(e.getMessage(), e.getStatus());
        } catch (Exception e) {
            return createErrorResponse("Failed to create task: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // @PostMapping("/create-with-user")
    // public ResponseEntity<Task> createTaskWithUser(
    //     @RequestBody Task task,
    //     @AuthenticationPrincipal User user
    // ) {
    //     return ResponseEntity.ok(taskService.createTask(task, task.getProject(), user));
    // }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<Task>> getProjectTasks(
        @PathVariable UUID projectId,
        @RequestParam(required = false) Task.TaskStatus status,
        @RequestParam(required = false) Task.Priority priority
    ) {
        // Implementation needed
        return null;
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Task> updateTaskStatus(
        @PathVariable Long id, 
        @RequestBody Task.TaskStatus status
    ) {
        return ResponseEntity.ok(taskService.updateTaskStatus(id, status));
    }

    @PutMapping("/{id}/assign")
    public ResponseEntity<Task> assignTask(
        @PathVariable Long id, 
        @RequestBody Long userId
    ) {
        // Implementation needed
        return null;
    }

    @GetMapping("/get-tasks")
    public ResponseEntity<Map<String, Object>> getAllTasks(
            @AuthenticationPrincipal User user,
            @RequestParam(defaultValue = "1", required = false) Integer pageNumber,
            @RequestParam(defaultValue = "10", required = false) Integer pageSize,
            @RequestParam(required = false) String date) {
        try {
            if (user == null) {
                throw new TaskException("User not authenticated", HttpStatus.UNAUTHORIZED);
            }

            List<Task> tasks = taskService.getTasksByUserWithPagination(user, pageNumber, pageSize, date);
            int totalCount = taskService.getAllTasksByUser(user).size();
            
            Map<String, Object> data = new HashMap<>();
            data.put("tasks", tasks);
            data.put("total_count", totalCount);
            
            return createSuccessResponse("Tasks fetched successfully", data);
        } catch (TaskException e) {
            return createErrorResponse(e.getMessage(), e.getStatus());
        } catch (Exception e) {
            return createErrorResponse("Failed to fetch tasks: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getTaskById(@PathVariable Long id) {
        try {
            Task task = taskService.getTaskById(id)
                .orElseThrow(() -> new TaskException("Task not found", HttpStatus.NOT_FOUND));
            
            return createSuccessResponse("Task fetched successfully", task);
        } catch (TaskException e) {
            return createErrorResponse(e.getMessage(), e.getStatus());
        } catch (Exception e) {
            return createErrorResponse("Failed to fetch task: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/edit/{id}")
    public ResponseEntity<Map<String, Object>> updateTask(@PathVariable Long id, @RequestBody Task updatedTask) {
        try {
            Task existingTask = taskService.getTaskById(id)
                .orElseThrow(() -> new TaskException("Task not found", HttpStatus.NOT_FOUND));

            validateTaskUpdate(updatedTask);
            updateTaskFields(existingTask, updatedTask);
            
            Task savedTask = taskService.updateTask(id, existingTask);
            return createSuccessResponse("Task updated successfully", savedTask);
        } catch (TaskException e) {
            return createErrorResponse(e.getMessage(), e.getStatus());
        } catch (Exception e) {
            return createErrorResponse("Failed to update task: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, Object>> deleteTask(@PathVariable Long id) {
        try {
            if (!taskService.deleteTask(id)) {
                throw new TaskException("Task not found", HttpStatus.NOT_FOUND);
            }
            return createSuccessResponse("Task deleted successfully", null);
        } catch (TaskException e) {
            return createErrorResponse(e.getMessage(), e.getStatus());
        } catch (Exception e) {
            return createErrorResponse("Failed to delete task: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Helper methods
    private ResponseEntity<Map<String, Object>> createSuccessResponse(String message, Object data) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", message);
        if (data != null) {
            response.put("data", data);
        }
        return ResponseEntity.ok(response);
    }

    private ResponseEntity<Map<String, Object>> createErrorResponse(String message, HttpStatus status) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", message);
        return ResponseEntity.status(status).body(response);
    }

    private void validateTaskUpdate(Task task) {
        if (task.getDescription() != null && task.getDescription().length() > 5000) {
            throw new TaskException("Description length should not exceed 5000 characters", HttpStatus.BAD_REQUEST);
        }
    }

    private void updateTaskFields(Task existingTask, Task updatedTask) {
        if (updatedTask.getTitle() != null) {
            existingTask.setTitle(updatedTask.getTitle());
        }
        if (updatedTask.getDescription() != null) {
            existingTask.setDescription(updatedTask.getDescription());
        }
        if (updatedTask.getDate() != null) {
            existingTask.setDate(updatedTask.getDate());
        }
        if (updatedTask.getStatus() != null) {
            existingTask.setStatus(updatedTask.getStatus());
        }
        if (updatedTask.getPriority() != null) {
            existingTask.setPriority(updatedTask.getPriority());
        }
    }
}
