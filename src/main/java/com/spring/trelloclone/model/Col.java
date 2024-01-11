package com.spring.trelloclone.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Col {

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
    private LocalDateTime createdDate;

    @ManyToOne
    @JoinColumn(name = "board_id")
    @JsonBackReference(value = "board-col")
    private  Board board;

    @OneToMany(mappedBy = "col", cascade = CascadeType.ALL)
    @JsonManagedReference(value = "col-task")
    private List<Task> taskList;

    public Col(){}

    public Col(Long id, String title, LocalDateTime createdDate, Board board, List<Task> taskList) {
        this.id = id;
        this.title = title;
        this.createdDate = createdDate;
        this.board = board;
        this.taskList = taskList;
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

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public List<Task> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
    }

}
