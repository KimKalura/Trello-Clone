package com.spring.trelloclone.service;

import com.spring.trelloclone.dto.TeamRequestDTO;
import com.spring.trelloclone.exceptions.UserNotFoundException;
import com.spring.trelloclone.model.Team;
import com.spring.trelloclone.model.Long;
import com.spring.trelloclone.repository.TeamRepository;
import com.spring.trelloclone.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class TeamService {

    private TeamRepository teamRepository;

    private UserRepository userRepository;

    @Autowired
    public TeamService(TeamRepository teamRepository, UserRepository userRepository) {
        this.teamRepository = teamRepository;
        this.userRepository = userRepository;
    }

    /*public Team addTeam(TeamRequestDTO teamRequestDTO) {
        //Cautam utilizatorii in DB si sa ii bagam intr-o lista la noi in Java (daca exista macar unul pe care nu il gasim dupa ID, aruncam exceptie)
        //Imi fac un Team X
        //Ii setez titlul la cel din DTO X
        //Ii setez descrierea la cea din DTO X
        //Adaugam userii gasiti in DB in lista de useri + utilizatorul logat in lista de useri
        //Salvam echipa in db X

        User foundUser = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found"));
        *//*List<Long> teamMembers = userRepository.findAllById(foundUser);*//*

        List<Long> teamMembers = userRepository.findAllById(teamRequestDTO.getUserIds());
        if (teamMembers.size() != teamRequestDTO.getUserIds().size()) {
            throw new UserNotFoundException("One or more users not found.");
        }
        Team team = new Team();
        team.setTitle(teamRequestDTO.getTitle());
        team.setDescription(teamRequestDTO.getDescription());

        teamMembers.add(foundUser);//user-ul logat
        team.setUserList(teamMembers);

        return teamRepository.save(team);
    }*/

}
