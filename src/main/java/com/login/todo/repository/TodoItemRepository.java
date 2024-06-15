package com.login.todo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.login.todo.modal.TodoItem;
import com.login.todo.modal.User;

import java.util.List;

@Repository
public interface TodoItemRepository extends JpaRepository<TodoItem, Long>{
    Page<TodoItem> findByUser(User user, Pageable pageable);
    List<TodoItem> findByUser(User user);
}
