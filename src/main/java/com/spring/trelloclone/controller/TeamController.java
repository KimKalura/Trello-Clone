package com.spring.trelloclone.controller;

import com.spring.trelloclone.dto.TeamRequestDTO;
import com.spring.trelloclone.model.Team;
import com.spring.trelloclone.model.User;
import com.spring.trelloclone.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PostMapping("/addMember/{userId}")
    public Team inviteMemberInATeam(@PathVariable Long userId) {
        return teamService.inviteMemberInATeam(userId);
    }

    @GetMapping("/findAllMembers")
    public List<User> getAllMembers() {
        return teamService.getAllMembers();
    }
}
