package com.spring.trelloclone.service;

import com.spring.trelloclone.dto.TeamRequestDTO;
import com.spring.trelloclone.exceptions.UserNotFoundException;
import com.spring.trelloclone.model.Role;
import com.spring.trelloclone.model.RoleType;
import com.spring.trelloclone.model.Team;
import com.spring.trelloclone.model.User;
import com.spring.trelloclone.repository.RoleRepository;
import com.spring.trelloclone.repository.TeamRepository;
import com.spring.trelloclone.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
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

    /*public Team addTeam(TeamRequestDTO teamRequestDTO) {
        //Cautam utilizatorii in DB si sa ii bagam intr-o lista la noi in Java (daca exista macar unul pe care nu il gasim dupa ID, aruncam exceptie)
        //Imi fac un Team X
        //Ii setez titlul la cel din DTO X
        //Ii setez descrierea la cea din DTO X
        //Adaugam userii gasiti in DB in lista de useri + utilizatorul logat in lista de useri
        //Salvam echipa in db X

        //TODO: aici trebuie pusi toti users: admin, leader, member?

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User foundUser = userRepository.findUserByUsername(userDetails.getUsername()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found"));

        List<User> teamMembers = userRepository.findAllById(teamRequestDTO.getUserIds());
        if (!teamMembers.contains(foundUser)) {
            teamMembers.add(foundUser);
        }
        Team team = new Team();
        team.setTitle(teamRequestDTO.getTitle());
        team.setDescription(teamRequestDTO.getDescription());

        teamMembers.add(foundUser);
        team.setUserList(teamMembers); ///???? trebuie lista de useri nu doar User !

        return teamRepository.save(team);
    }*/

    /*public Team inviteMember(Long userId) {
        //Pentru inceput, pur si simplu se va adauga un nou utilizator existent (dar care sa aiba rolul de team_member) in echipa, fara a mai fi invitat inainte
        //Cautam utilizatorul in DB dupa ID (daca nu exista, aruncam exceptie)
        //Fac legatura intre utilizator si echipa - ??? fac un nou member user + team ???
        //Salvam echipa in db (updatez echipa)

        //UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //User foundUser = userRepository.findUserByUsername(userDetails.getUsername()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found"));

        //verificam daca userul este deja un TEAM_MEMBER

        User invitedMemberUser = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User to be invited not found"));
        if (invitedMemberUser.getTeamList() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User is already a member of a team");
        }

        Team teamMember = new Team();
        teamMember.setUser(invitedMemberUser);
        teamMember.getUser().setRoleList(RoleType.TEAM_MEMBER);

        Team team = invitedMemberUser.getTeamList(); // userul invitat apartine deja unui team
        team.addTeamMemberInATeam(teamMember);

        teamRepository.save(team);

        return team;
    }*/



}
