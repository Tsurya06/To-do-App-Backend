package com.login.task.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.login.task.modal.Task;
import com.login.task.modal.User;
import com.login.task.services.TaskServiceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    @Autowired
    private TaskServiceImpl TaskService;

    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<Task>> getProjectTasks(
        @PathVariable UUID projectId,
        @RequestParam(required = false) Task.TaskStatus status,
        @RequestParam(required = false) Task.Priority priority
    ) {
        // Implementation needed
        return null; // Placeholder return, actual implementation needed
    }

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        // Implementation needed
        return null; // Placeholder return, actual implementation needed
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Task> updateTaskStatus(
        @PathVariable Long id, 
        @RequestBody Task.TaskStatus status
    ) {
        // Implementation needed
        return null; // Placeholder return, actual implementation needed
    }

    @PutMapping("/{id}/assign")
    public ResponseEntity<Task> assignTask(
        @PathVariable Long id, 
        @RequestBody Long userId
    ) {
        // Implementation needed
        return null; // Placeholder return, actual implementation needed
    }

    @GetMapping("/get-tasks")
    public ResponseEntity<Map<String, Object>> getAllTasks(
        @AuthenticationPrincipal User user,
        @RequestParam(defaultValue = "1", required = false) Integer pageNumber,
        @RequestParam(defaultValue = "10", required = false) Integer pageSize,
        @RequestParam(required = false) String date
    ) {
        try {
            List<Task> Tasks = TaskService.getTasksByUserWithPagination(user, pageNumber, pageSize, date);
            int totalCount = TaskService.getAllTasksByUser(user).size();
            
            Map<String, Object> response = new HashMap<>();
            response.put("total_count", totalCount);
            response.put("success", true);
            response.put("data", Tasks);
            response.put("message", "Fetched All Tasks Successfully!");
            
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {     
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        try {
            Optional<Task> Task = TaskService.getTaskById(id);
            if (Task.isPresent()) {
                return ResponseEntity.ok().body(Task.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createTask(@RequestBody Task Task,@AuthenticationPrincipal User user) {
        try {
            if (Task.getDescription() != null && Task.getDescription().length() <= 5000) {
                Task.setUser(user);
                TaskService.createTask(Task);
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Task Created Successfully!");
                return ResponseEntity.status(HttpStatus.CREATED).body(response);
            } else {
                throw new IllegalArgumentException("Description length should not exceed 5000 characters");
            }
        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PatchMapping("/edit/{id}")
    public ResponseEntity<Map<String, Object>> updateTask(@PathVariable Long id, @RequestBody Task Task) {
        try {
            Optional<Task> existingTask = TaskService.getTaskById(id);
            if (existingTask.isPresent()) {
                Task task = existingTask.get();
                if (Task.getTitle() != null) {
                    task.setTitle(Task.getTitle());
                }
                if (Task.getDescription() != null && Task.getDescription().length() <= 5000) {
                    task.setDescription(Task.getDescription());
                } else {
                    throw new IllegalArgumentException("Description length should not exceed 5000 characters");
                }
                if (Task.getDate() != null) {
                    task.setDate(Task.getDate());
                }
                TaskService.updateTask(id, task); 
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "task Edited Successfully!");
                return ResponseEntity.status(HttpStatus.OK).body(response);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, Object>> deleteTask(@PathVariable Long id) {
        try {
            boolean deleted = TaskService.deleteTask(id);
            Map<String, Object> response = new HashMap<>();
            if (deleted) {
                response.put("success", true);
                response.put("message", "task deleted successfully!");
                return ResponseEntity.ok().body(response);
            } else {
                response.put("success", false);
                response.put("message", "task not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
