package com.login.todo.services;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.login.todo.modal.RefreshToken;
import com.login.todo.repository.RefreshTokenReposatory;

@Service
public class RefreshTokenService {
    @Autowired
    private RefreshTokenReposatory refreshTokenReposatory;

    public RefreshToken createRefreshToken(String id, String username) {
        RefreshToken token = new RefreshToken();
        token.setId(id);
        token.setRefreshToken(username);
        token.setRefreshExpiration(Instant.now().plusMillis(86400000));
        return this.refreshTokenReposatory.save(token);
    }
    
    

}
