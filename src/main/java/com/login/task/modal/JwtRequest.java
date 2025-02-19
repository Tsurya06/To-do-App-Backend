package com.login.task.modal;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class JwtRequest {
    private String username;
    private String email;
    private String password;
}
