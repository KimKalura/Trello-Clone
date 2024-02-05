package com.spring.trelloclone.controller;

import com.spring.trelloclone.dto.TeamRequestDTO;
import com.spring.trelloclone.model.Team;
import com.spring.trelloclone.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/team")
public class TeamController {

    private TeamService teamService;

    @Autowired
    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @PostMapping("/create")
    public Team addTeam(@RequestBody TeamRequestDTO teamRequestDTO) {
        return teamService.addTeam(teamRequestDTO);
    }

    @PostMapping("addMember/{userId}")
    public Team inviteMember(@PathVariable Long userId) {
        return teamService.inviteMember(userId);
    }

}
