package com.spring.trelloclone.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
//import org.springframework.data.annotation.Id;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class TaskHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "category_seq")
    @SequenceGenerator(name = "category_seq",
            sequenceName = "category_seq",
            initialValue = 1,
            allocationSize = 1)
    private Long id;

    @Column
    private LocalDateTime movementDate;

    @Column
    private String columnName;

    @ManyToOne
    @JoinColumn(name = "task_id")
    @JsonBackReference(value = "task-taskHistory")
    private Task task;

    public TaskHistory(){}

    public TaskHistory(Long id, LocalDateTime movementDate, String columnName, Task task) {
        this.id = id;
        this.movementDate = movementDate;
        this.columnName = columnName;
        this.task = task;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public LocalDateTime getMovementDate() {
        return movementDate;
    }

    public void setMovementDate(LocalDateTime movementDate) {
        this.movementDate = movementDate;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }
}
