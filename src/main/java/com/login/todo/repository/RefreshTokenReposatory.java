package com.login.todo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.login.todo.modal.RefreshToken;

@Repository
public interface RefreshTokenReposatory extends JpaRepository<RefreshToken, String>{
    
}