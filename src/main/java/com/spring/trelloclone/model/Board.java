package com.spring.trelloclone.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Board {

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
    private LocalDateTime createdDate;


    @ManyToOne
    @JoinColumn(name = "team_id")
    @JsonBackReference(value = "team-board")
    private Team team;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    @JsonManagedReference(value = "board-col")
    private List<Col> columnList;

    public Board(){}

    public Board(Long id, String title, String description, LocalDateTime createdDate, Team team, List<Col> columnList) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.createdDate = createdDate;
        this.team = team;
        this.columnList = columnList;
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

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public List<Col> getColumnList() {
        return columnList;
    }

    public void setColumnList(List<Col> columnList) {
        this.columnList = columnList;
    }

}
