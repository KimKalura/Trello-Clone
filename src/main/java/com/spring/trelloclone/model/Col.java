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
    @JsonBackReference(value = "column-board")
    private  Board board;

    @OneToMany(mappedBy = "column", cascade = CascadeType.ALL)
    @JsonManagedReference(value = "column-task")
    private List<Task> taskList;


}
