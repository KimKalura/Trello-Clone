package com.spring.trelloclone.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "category_seq")
    @SequenceGenerator(name = "category_seq",
            sequenceName = "category_seq",
            initialValue = 1,
            allocationSize = 1)
    private Long id;

    @Column
    private String title;

    @Column
    private String description;

    @Column
    private User assignee; //**

    @Column
    private LocalDateTime createdDate;

    @Column
    private LocalDateTime deadline;

    @ManyToOne
    @JoinColumn(name = "column_id")
    @JsonBackReference(value = "task-column")
    private Col col;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL)
    @JsonManagedReference(value = "task-step")
    private List<Step> stepList;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL)  //**
    @JsonManagedReference(value = "task-task-history")
    private List<TaskHistory> taskHistoryList;
}
