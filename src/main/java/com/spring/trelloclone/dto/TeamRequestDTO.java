package com.spring.trelloclone.dto;

import java.util.List;
import java.util.Set;

public class TeamRequestDTO {

    private String title;
    private String description;
    private Set<Long> userIds;

    public TeamRequestDTO(String title, String description, Set<Long> longIds) {
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

    public Set<Long> getUserIds() {
        return userIds;
    }

    public void setUserIds(Set<Long> longIds) {
        this.userIds = longIds;
    }
}
