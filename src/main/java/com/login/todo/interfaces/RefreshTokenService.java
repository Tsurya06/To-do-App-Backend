package com.login.todo.interfaces;

import com.login.todo.modal.RefreshToken;

public interface RefreshTokenService {
    RefreshToken createRefreshToken(String username);
    RefreshToken verifyRefreshToken(String refeshToken);
}
