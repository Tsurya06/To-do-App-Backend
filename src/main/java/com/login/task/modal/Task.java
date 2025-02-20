package com.login.task.modal;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.login.task.utils.CustomLocalDateTimeDeserializer;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Setter
@Getter
@AllArgsConstructor
@Table
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "title", nullable = false)
    private String title;
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "date", nullable = false)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime date;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TaskStatus status = TaskStatus.TO_DO;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority", nullable = false)
    private Priority priority = Priority.MEDIUM;


    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    // @Column(name = "due_date")
    // @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    // private LocalDateTime dueDate;

    // @Column(name = "created_at", nullable = false)
    // private LocalDateTime createdAt;

    // @Column(name = "updated_at", nullable = false)
    // private LocalDateTime updatedAt;

    public enum TaskStatus {
        TO_DO("To Do"),
        IN_PROGRESS("In Progress"),
        COMPLETED("Completed"),
        ON_HOLD("On Hold");

        private final String value;

        TaskStatus(String value) {
            this.value = value;
        }

        @JsonValue
        public String getValue() {
            return value;
        }

        @JsonCreator
        public static TaskStatus fromValue(String value) {
            for (TaskStatus status : TaskStatus.values()) {
                if (status.value.equalsIgnoreCase(value)) {
                    return status;
                }
            }
            throw new IllegalArgumentException("Unknown TaskStatus: " + value);
        }
    }

    public enum Priority {
        HIGH("High"),
        MEDIUM("Medium"),
        LOW("Low");

        private final String value;

        Priority(String value) {
            this.value = value;
        }

        @JsonValue
        public String getValue() {
            return value;
        }

        @JsonCreator
        public static Priority fromValue(String value) {
            for (Priority priority : Priority.values()) {
                if (priority.value.equalsIgnoreCase(value)) {
                    return priority;
                }
            }
            throw new IllegalArgumentException("Unknown Priority: " + value);
        }
    }

    // @PrePersist
    // protected void onCreate() {
    //     createdAt = LocalDateTime.now();
    //     updatedAt = LocalDateTime.now();
    // }

    // @PreUpdate
    // protected void onUpdate() {
    //     updatedAt = LocalDateTime.now();
    // }

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User assignee;

    @Override
    public String toString() {
    return "Task{" +
        "id=" + id +
        ", title='" + title + '\'' +
        ", description='" + description + '\'' +
        ", date=" + date +
        ", status=" + status +
        ", priority=" + priority +
        ", project=" + project +
        ", comments=" + comments +
        // ", dueDate=" + dueDate +
        '}';
    }
    
}
