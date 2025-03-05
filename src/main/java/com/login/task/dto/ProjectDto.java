package com.login.task.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.login.task.modal.Task;
import com.login.task.modal.User;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProjectDto {
    private String id;
    private String name;
    private String description;
    private User user;
    private List<Task> tasks;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;    
    
}
