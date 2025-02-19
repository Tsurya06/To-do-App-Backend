package com.login.task.interfaces;

import com.login.task.modal.User;

public interface UserService {
    boolean isUserExists(Long user_id);
    User signUp(User user);
    User login(String email, String password);
}
