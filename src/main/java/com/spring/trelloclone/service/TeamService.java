package com.spring.trelloclone.service;

import com.spring.trelloclone.dto.TeamRequestDTO;
import com.spring.trelloclone.model.Role;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
    public Team addTeam(TeamRequestDTO teamRequestDTO){
        List<User> users = userRepository.findAllById(teamRequestDTO.getUserIds());
        if (users.size() != teamRequestDTO.getUserIds().size()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found");//throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found")
        }
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User foundUser = userRepository.findUserByUsername(userDetails.getUsername()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found"));
        Team team = new Team();
        team.setTitle(teamRequestDTO.getTitle());
        team.setDescription(teamRequestDTO.getDescription());

        team.getUsers().addAll(users);
        team.getUsers().add(foundUser);

        foundUser.getTeam().add(team); //face legatura cu tabela user_team

        return teamRepository.save(team);
    }

    // >>Invit un utilizator in echipa mea (admin, team-leader)
    // >> Pentru inceput, pur si simplu se va adauga un nou utilizator existent (dar care sa aiba rolul de team_member) in echipa, fara a mai fi invitat inainte
    //Cautam utilizatorul in DB dupa ID (daca nu exista, aruncam exceptie)
    //Fac legatura intre utilizator si echipa
    //Salvam echipa in db (updatez echipa)
    public Team inviteMemberInATeam(Long userId, Long teamId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        Team team = teamRepository.findById(teamId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Team not found"));
        team.getUsers().add(user);
        return teamRepository.save(team);
    }

    //nu se vad userii in team
    public List<User> getAllMembers() { //*
        return teamRepository.findAll().stream()
                .flatMap(team -> team.getUsers().stream())
                .filter(user -> user.getRoleList().contains(RoleType.TEAM_MEMBER))
                .collect(Collectors.toList());
    }


    //Sterg un utilizator din echipa mea (admin, team-leader)
    //Ce pasi facem in service?
    //Cautam utilizatorul in DB dupa ID (daca nu exista, aruncam exceptie)
    //Stergem utilizatorul gasit din echipa
    //Ce iese din Java?
    //Team (team-ul updatat)
    public void deleteUserFromTeam(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        Set<Team> teams = user.getTeam();
        for (Team team : teams) {
            team.getUsers().remove(user);
            teamRepository.save(team);
        }
    }

    //Vad toate echipele (admin, team_leader, team_member):
    //id, title, description, lista membrii, +boardlist?
    //Luam toate echipe din DB
    public List<Team> getAllTeams() {
        return teamRepository.findAll();
      /*List<Team> foundTeam = teamRepository.findAll();
        return foundTeam.stream()
                .filter(team -> team.getUsers().equals(RoleType.ROLE_ADMIN) || team.getUsers().equals(RoleType.TEAM_LEADER) || team.getUsers().equals(RoleType.TEAM_MEMBER))
                .collect(Collectors.toList());*/
    }

    public List<Team> getTeamsOfLoggedInUser() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User foundUser = userRepository.findUserByUsername(userDetails.getUsername()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found"));
        Set<Team> foundTeam =  foundUser.getTeam();
        return new ArrayList<>(foundTeam);
    }

}