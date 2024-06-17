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

    @Column
    private String text;

    @Column
    private boolean checked;

    @ManyToOne
    @JoinColumn(name = "task_id")
    @JsonBackReference(value = "task-step")
    private Task task;

    public Step(){}

    public Step(Long id, String text, boolean checked, Task task) {
        this.id = id;
        this.text = text;
        this.checked = checked;
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
