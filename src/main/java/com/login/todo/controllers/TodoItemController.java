package com.login.todo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.login.todo.modal.TodoItem;
import com.login.todo.modal.User;
import com.login.todo.services.TodoItemServiceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/todo")
public class TodoItemController {
    @Autowired
    private TodoItemServiceImpl todoItemService;

    @GetMapping("/get-todos")
    public ResponseEntity<Map<String, Object>> getAllTodoItems(@AuthenticationPrincipal User user,@RequestParam(defaultValue = "1", required = false) Integer pageNumber, @RequestParam(defaultValue = "10", required = false) Integer pageSize) {
        try {
            List<TodoItem> todoItems = todoItemService.getTodosByUserWithPagination(user, pageNumber, pageSize);
            int totalCount = todoItemService.getAllTodosByUser(user).size();
            Map<String, Object> response = new HashMap<>();
            response.put("total_count", totalCount);
            response.put("success", true);
            response.put("data", todoItems);
            response.put("message", "Fetched All Todos");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {     
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<TodoItem> getTodoItemById(@PathVariable Long id) {
        try {
            Optional<TodoItem> todoItem = todoItemService.getTodoItemById(id);
            if (todoItem.isPresent()) {
                return ResponseEntity.ok().body(todoItem.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createTodoItem(@RequestBody TodoItem todoItem,@AuthenticationPrincipal User user) {
        try {
            if (todoItem.getDescription() != null && todoItem.getDescription().length() <= 5000) {
                todoItem.setUser(user);
                todoItemService.createTodoItem(todoItem);
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Todo Added Successfully!");
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
    public ResponseEntity<Map<String, Object>> updateTodoItem(@PathVariable Long id, @RequestBody TodoItem todoItem) {
        try {
            Optional<TodoItem> existingTodoItem = todoItemService.getTodoItemById(id);
            if (existingTodoItem.isPresent()) {
                TodoItem todo = existingTodoItem.get();
                if (todoItem.getTitle() != null) {
                    todo.setTitle(todoItem.getTitle());
                }
                if (todoItem.getDescription() != null && todoItem.getDescription().length() <= 5000) {
                    todo.setDescription(todoItem.getDescription());
                } else {
                    throw new IllegalArgumentException("Description length should not exceed 5000 characters");
                }
                if (todoItem.getDate() != null) {
                    todo.setDate(todoItem.getDate());
                }
                todoItemService.updateTodoItem(id, todo); 
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Todo Edited Successfully!");
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
    public ResponseEntity<Map<String, Object>> deleteTodoItem(@PathVariable Long id) {
        try {
            boolean deleted = todoItemService.deleteTodoItem(id);
            Map<String, Object> response = new HashMap<>();
            if (deleted) {
                response.put("success", true);
                response.put("message", "Todo deleted successfully!");
                return ResponseEntity.ok().body(response);
            } else {
                response.put("success", false);
                response.put("message", "Todo not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
