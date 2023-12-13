package com.example.taskmanager.data;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String description;

    @Future(message = "Due date must be in the future")
    private LocalDate dueDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private AppUser assignedUser;

    @Enumerated(EnumType.STRING)
    private TaskState state;
}

