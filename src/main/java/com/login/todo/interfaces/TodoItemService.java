package com.login.todo.interfaces;

import java.util.List;
import java.util.Optional;

import com.login.todo.modal.TodoItem;
import com.login.todo.modal.User;

public interface TodoItemService {
    List<TodoItem> getTodosByUserWithPagination(User user, int offset, int limit);
    List<TodoItem> getAllTodosByUser(User user);
    List<TodoItem> getAllTodoItems();
    Optional<TodoItem> getTodoItemById(Long id);
    TodoItem createTodoItem(TodoItem todoItem);
    TodoItem updateTodoItem(Long id, TodoItem todoItem);
    boolean deleteTodoItem(Long id);
}
