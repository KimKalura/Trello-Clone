package com.spring.trelloclone.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.springframework.data.annotation.Id;

import javax.persistence.*;
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
    private String assigneeUser;

    @Column
    private LocalDateTime createdDate;

    @Column
    private LocalDateTime deadline;

    @ManyToOne
    @JoinColumn(name = "col_id")
    @JsonBackReference(value = "col-task")
    private Col col;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL)// (checklist - o lista de pasi pentru rezolvarea task-ului)
    @JsonManagedReference(value = "task-step")
    private List<Step> stepList;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL)
    @JsonManagedReference(value = "task-taskHistory")
    private List<TaskHistory> taskHistoryList;

    public Task(){}

    public Task(Long id, String title, String description, String assigneeUser, LocalDateTime createdDate, LocalDateTime deadline, Col col, List<Step> stepList, List<TaskHistory> taskHistoryList) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.assigneeUser = assigneeUser;
        this.createdDate = createdDate;
        this.deadline = deadline;
        this.col = col;
        this.stepList = stepList;
        this.taskHistoryList = taskHistoryList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAssigneeUser() {
        return assigneeUser;
    }

    public void setAssigneeUser(String assigneeUser) {
        this.assigneeUser = assigneeUser;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public Col getCol() {
        return col;
    }

    public void setCol(Col col) {
        this.col = col;
    }

    public List<Step> getStepList() {
        return stepList;
    }

    public void setStepList(List<Step> stepList) {
        this.stepList = stepList;
    }

    public List<TaskHistory> getTaskHistoryList() {
        return taskHistoryList;
    }

    public void setTaskHistoryList(List<TaskHistory> taskHistoryList) {
        this.taskHistoryList = taskHistoryList;
    }

}
