package com.login.task.dto;

import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class UserDTO {
    private Long id;
    private String name;
    private String email;
    private String createdAt;
    private int taskCount;
    private int projectCount;
    private String role;
} 