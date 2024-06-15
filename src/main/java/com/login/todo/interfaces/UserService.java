package com.login.todo.interfaces;

import com.login.todo.modal.User;

public interface UserService {
    boolean isUserExists(String user_id);
    User signUp(User user);
    User login(String email, String password);
}
