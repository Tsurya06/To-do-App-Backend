package com.login.todo.services;

import java.time.Instant;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.login.todo.interfaces.RefreshTokenService;
import com.login.todo.modal.RefreshToken;
import com.login.todo.modal.User;
import com.login.todo.repository.RefreshTokenReposatory;
import com.login.todo.repository.UserRepository;

import lombok.Builder;

@Service
@Builder
public class RefreshTokenServiceImpl implements RefreshTokenService{
    private final long refreshTokenValidity = 48*60*60*1000; //24hrs

    @Autowired
    private RefreshTokenReposatory refreshTokenReposatory;

    @Autowired
    private UserRepository userRepository;

    public RefreshToken createRefreshToken(String username) {
        User user = userRepository.findByEmail(username);
        RefreshToken refToken = user.getRefreshToken();
        if(refToken == null) {
            refToken= RefreshToken.builder()
            .refreshToken(UUID.randomUUID().toString())
            .refreshExpiration(Instant.now().plusMillis(refreshTokenValidity))
            .user(userRepository.findByEmail(username))
            .build();

        }else {
            refToken.setRefreshExpiration(Instant.now().plusMillis(refreshTokenValidity));
        }
        refreshTokenReposatory.save(refToken);

        return refToken;
    }


    public RefreshToken verifyRefreshToken(String refeshToken) {
        RefreshToken refreshTokenObj = refreshTokenReposatory.findByRefreshToken(refeshToken).orElseThrow(() -> new RuntimeException("Invalid Refresh Token"));

        if(refreshTokenObj.getRefreshExpiration().compareTo(Instant.now())<0) {
            refreshTokenReposatory.delete(refreshTokenObj);
            throw new RuntimeException("Refresh Token Expired!");
        }
        return refreshTokenObj;
    }

}
