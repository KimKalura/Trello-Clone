package com.spring.trelloclone.dto;

import com.spring.trelloclone.model.Long;

import java.util.List;

public class TeamRequestDTO {

    private String title;
    private String description;
    private List<Long> userIds;

    public TeamRequestDTO(String title, String description, List<Long> longIds) {
        this.title = title;
        this.description = description;
        this.userIds = longIds;
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

    public List<Long> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<Long> longIds) {
        this.userIds = longIds;
    }
}
