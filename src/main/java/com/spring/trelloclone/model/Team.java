package com.spring.trelloclone.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;


import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Team {

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

   /* @OneToMany //(mappedBy = "team")
    @JoinColumn(name = "user_id")
    @JsonBackReference(value = "user-team")
    private Set<User> userSet;*/

    @ManyToMany(mappedBy = "team")
    @JsonIgnoreProperties("team")
    private Set<User> users;


    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL)
    @JsonManagedReference(value = "team-board")
    private List<Board> boardList;

    public Team(){}

    public Team(Long id, String title, String description, Set<User> users, List<Board> boardList) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.users = users;
        this.boardList = boardList;
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

    public Set<User> getUsers() {
        if (this.users == null) {
            this.users = new HashSet<>();
        }
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public List<Board> getBoardList() {
        return boardList;
    }

    public void setBoardList(List<Board> boardList) {
        this.boardList = boardList;
    }

}
