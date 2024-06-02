package com.login.todo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.login.todo.modal.TodoItem;
import com.login.todo.modal.User;

import java.util.List;

public interface TodoItemRepository extends JpaRepository<TodoItem, Long>{
    List<TodoItem> findByUser(User user);
}
