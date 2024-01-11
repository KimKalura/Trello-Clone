package com.spring.trelloclone.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
//import org.springframework.data.annotation.Id;

import javax.persistence.*;

@Entity
public class Step {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "category_seq")
    @SequenceGenerator(name = "category_seq",
            sequenceName = "category_seq",
            initialValue = 1,
            allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "task_id")
    @JsonBackReference(value = "task-step")
    private Task task;

    public Step(){}

    public Step(Long id, Task task) {
        this.id = id;
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
}
