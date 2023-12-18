package com.example.taskmanager.data;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String description;

    private LocalDate dueDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private AppUser assignedUser;

    @Enumerated(EnumType.STRING)
    private TaskState state;
}

