package com.login.task.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.login.task.modal.RefreshToken;

@Repository
public interface RefreshTokenReposatory extends JpaRepository<RefreshToken, Integer>{
    Optional<RefreshToken> findByRefreshToken(String refreshToken);
}