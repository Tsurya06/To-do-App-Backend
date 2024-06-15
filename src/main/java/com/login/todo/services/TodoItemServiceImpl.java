package com.login.todo.services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import com.login.todo.interfaces.TodoItemService;
import com.login.todo.modal.TodoItem;
import com.login.todo.modal.User;
import com.login.todo.repository.TodoItemRepository;

import java.util.List;
import java.util.Optional;

@Service
public class TodoItemServiceImpl implements TodoItemService {
    @Autowired
    private TodoItemRepository todoItemRepository;
    
    @Override
    public List<TodoItem> getTodosByUserWithPagination(User user, int offset, int limit) {
        Pageable pageable = PageRequest.of(offset / limit, limit);
        Page<TodoItem> todoPage = todoItemRepository.findByUser(user, pageable);
        return todoPage.getContent();
    }
    @Override
    public List<TodoItem> getAllTodosByUser(User user) {
        return todoItemRepository.findByUser(user);
    }
    @Override
    public List<TodoItem> getAllTodoItems() {
        return todoItemRepository.findAll();
    }
    @Override
    public Optional<TodoItem> getTodoItemById(Long id) {
        return todoItemRepository.findById(id);
    }
    @Override
    public TodoItem createTodoItem(TodoItem todoItem) {
        return todoItemRepository.save(todoItem);
    }
    @Override
    public TodoItem updateTodoItem(Long id, TodoItem todoItem) {
        if (todoItemRepository.existsById(id)) {
            todoItem.setId(id);
            return todoItemRepository.save(todoItem);
        }
        return null;
    }
    @Override
    public boolean deleteTodoItem(Long id) {
        if (todoItemRepository.existsById(id)) {
            todoItemRepository.deleteById(id);
            return true;
        }
        return false;
    }

}

