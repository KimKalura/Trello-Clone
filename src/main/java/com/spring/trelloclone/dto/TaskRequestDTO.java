package com.spring.trelloclone.dto;

import com.spring.trelloclone.model.Step;
import com.spring.trelloclone.model.User;

import java.time.LocalDateTime;
import java.util.List;

public class TaskRequestDTO {

    private String title;
    private String description;
    private LocalDateTime deadline;

    private Long columnId;
    private List<Step> steps;

    private Long assigneeId;

    public TaskRequestDTO(String title, String description, LocalDateTime deadline, Long columnId, List<Step> steps, Long assigneeId) {
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.columnId = columnId;
        this.steps = steps;
        this.assigneeId = assigneeId;
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

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public Long getColumnId() {
        return columnId;
    }

    public void setColumnId(Long columnId) {
        this.columnId = columnId;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    public Long getAssigneeId() {
        return assigneeId;
    }

    public void setAssigneeId(Long assigneeId) {
        this.assigneeId = assigneeId;
    }
}
