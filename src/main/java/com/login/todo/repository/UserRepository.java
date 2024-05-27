package com.login.todo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.login.todo.modal.User;

public interface UserRepository extends JpaRepository<User,Long> {

    User findByUsername(String username);
    User findByEmail(String email);
}
