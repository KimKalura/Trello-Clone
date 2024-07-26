package com.spring.trelloclone.dto;

import com.spring.trelloclone.model.*;

import java.time.LocalDateTime;
import java.util.List;

public class TaskResponseDTO {

    private Long id;
    private String title;
    private String description;
    private User assigneeUser;
    private LocalDateTime createdDate;
    private LocalDateTime deadline;
    private List<Step> checklist;
    private Col taskColumn;
    private Board taskBoard;


    public TaskResponseDTO() {
    }

    public TaskResponseDTO(Long id, String title, String description, User assigneeUser, LocalDateTime createdDate, LocalDateTime deadline, List<Step> checklist, Col taskColumn, Board taskBoard) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.assigneeUser = assigneeUser;
        this.createdDate = createdDate;
        this.deadline = deadline;
        this.checklist = checklist;
        this.taskColumn = taskColumn;
        this.taskBoard = taskBoard;
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

    public User getAssigneeUser() {
        return assigneeUser;
    }

    public void setAssigneeUser(User assigneeUser) {
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

    public List<Step> getChecklist() {
        return checklist;
    }

    public void setChecklist(List<Step> checklist) {
        this.checklist = checklist;
    }

    public Col getTaskColumn() {
        return taskColumn;
    }

    public void setTaskColumn(Col taskColumn) {
        this.taskColumn = taskColumn;
    }

    public Board getTaskBoard() {
        return taskBoard;
    }

    public void setTaskBoard(Board taskBoard) {
        this.taskBoard = taskBoard;
    }
}
