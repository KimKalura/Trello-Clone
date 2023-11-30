package com.spring.trelloclone.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

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
    @JsonManagedReference(value = "board-column")
    private List<Col> columnList;


}
