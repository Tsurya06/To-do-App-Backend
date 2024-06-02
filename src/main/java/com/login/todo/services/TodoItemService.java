package com.login.todo.services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.login.todo.modal.TodoItem;
import com.login.todo.modal.User;
import com.login.todo.repository.TodoItemRepository;

import java.util.List;
import java.util.Optional;

@Service
public class TodoItemService {
    @Autowired
    private TodoItemRepository todoItemRepository;

    public List<TodoItem> getAllTodosByUser(User user) {
        return todoItemRepository.findByUser(user);
    }

    public List<TodoItem> getAllTodoItems() {
        return todoItemRepository.findAll();
    }

    public Optional<TodoItem> getTodoItemById(Long id) {
        return todoItemRepository.findById(id);
    }

    public TodoItem createTodoItem(TodoItem todoItem) {
        return todoItemRepository.save(todoItem);
    }

    public TodoItem updateTodoItem(Long id, TodoItem todoItem) {
        if (todoItemRepository.existsById(id)) {
            todoItem.setId(id);
            return todoItemRepository.save(todoItem);
        }
        return null;
    }

    public boolean deleteTodoItem(Long id) {
        if (todoItemRepository.existsById(id)) {
            todoItemRepository.deleteById(id);
            return true;
        }
        return false;
    }
}

