package com.spring.trelloclone.service;

import com.spring.trelloclone.dto.TeamRequestDTO;
import com.spring.trelloclone.model.RoleType;
import com.spring.trelloclone.model.Team;
import com.spring.trelloclone.model.User;
import com.spring.trelloclone.repository.RoleRepository;
import com.spring.trelloclone.repository.TeamRepository;
import com.spring.trelloclone.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TeamService {

    private TeamRepository teamRepository;

    private UserRepository userRepository;
    private RoleRepository roleRepository;


    @Autowired
    public TeamService(TeamRepository teamRepository, UserRepository userRepository, RoleRepository roleRepository) {
        this.teamRepository = teamRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    //Cautam utilizatorii in DB si sa ii bagam intr-o lista la noi in Java (daca exista macar unul pe care nu il gasim dupa ID, aruncam exceptie)
    //Imi fac un Team
    //Ii setez titlul la cel din DTO
    //Ii setez descrierea la cea din DTO
    //Adaugam userii gasiti in DB in lista de useri + utilizatorul logat in lista de useri
    //Salvam echipa in db
    public Team addTeam(TeamRequestDTO teamRequestDTO) throws ChangeSetPersister.NotFoundException {
        List<User> users = userRepository.findAllById(teamRequestDTO.getUserIds());
        if (users.size() != teamRequestDTO.getUserIds().size()) {
            throw new ChangeSetPersister.NotFoundException();
        }

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User foundUser = userRepository.findUserByUsername(userDetails.getUsername()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found"));

        Team team = new Team();
        team.setTitle(teamRequestDTO.getTitle());
        team.setDescription(teamRequestDTO.getDescription());

        team.getUsers().addAll(users);
        team.getUsers().add(foundUser);

        return teamRepository.save(team);
    }


    // >>Invit un utilizator in echipa mea (admin, team-leader)
    // >> Pentru inceput, pur si simplu se va adauga un nou utilizator existent (dar care sa aiba rolul de team_member) in echipa, fara a mai fi invitat inainte
    //Cautam utilizatorul in DB dupa ID (daca nu exista, aruncam exceptie)
    //Fac legatura intre utilizator si echipa - ??? fac un nou member user + team ???
    //Salvam echipa in db (updatez echipa)
    //TODO: nu trebuie dat ca parametru si teamId pt a stii exact in care echipa TEAM_MEMBER sa fie adaugat ??!?

    public Team inviteMemberInATeam(Long userId, Long teamId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        Team team = teamRepository.findById(teamId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Team not found"));
        team.getUsers().add(user);
        return teamRepository.save(team);
    }

    public List<User> getAllMembers() { //*
        return teamRepository.findAll().stream()
                .flatMap(team -> team.getUsers().stream())
                .filter(user -> user.getRoleList().contains(RoleType.TEAM_MEMBER))
                .collect(Collectors.toList());
    }
}
