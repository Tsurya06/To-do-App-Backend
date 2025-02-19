package com.login.task.interfaces;

import com.login.task.modal.RefreshToken;

public interface RefreshTokenService {
    RefreshToken createRefreshToken(String username);
    RefreshToken verifyRefreshToken(String refeshToken);
}
