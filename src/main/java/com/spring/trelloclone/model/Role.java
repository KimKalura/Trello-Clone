package com.spring.trelloclone.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.persistence.Column;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Role {

    @Id
    @GeneratedValue
    private java.lang.Long id;

    @Column
    private RoleType roleType;

    @ManyToMany(mappedBy = "roleList")
    @JsonIgnoreProperties("roleList")
    private List<Long> userList;

    public Role(){}


    public java.lang.Long getId() {
        return id;
    }

    public RoleType getRoleType() {
        return roleType;
    }

    public void setRoleType(RoleType roleType) {
        this.roleType = roleType;
    }

    public List<Long> getUserList() {
        if (userList == null){
            userList = new ArrayList<>();
        }
        return userList;
    }

    public void setUserList(List<Long> userList) {
        this.userList = userList;
    }

}
