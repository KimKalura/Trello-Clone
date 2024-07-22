package com.spring.trelloclone.service;

import com.spring.trelloclone.model.Board;
import com.spring.trelloclone.model.Team;
import com.spring.trelloclone.model.User;
import com.spring.trelloclone.repository.BoardRepository;
import com.spring.trelloclone.repository.ColRepository;
import com.spring.trelloclone.repository.TeamRepository;
import com.spring.trelloclone.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
public class BoardService {

    private BoardRepository boardRepository;
    private TeamRepository teamRepository;
    private ColRepository colRepository;
    private UserRepository userRepository;


    @Autowired
    public BoardService(BoardRepository boardRepository, TeamRepository teamRepository, ColRepository colRepository, UserRepository userRepository) {
        this.boardRepository = boardRepository;
        this.teamRepository = teamRepository;
        this.colRepository = colRepository;
        this.userRepository = userRepository;
    }


    public Board addBoard(Board board, Long teamId) {
        Board foundBoard = boardRepository.findBoardByTitle(board.getTitle());
        if (foundBoard != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Board already exists");
        }
        Team team = teamRepository.findById(teamId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Team not found"));
        board.setTeam(team);
        return boardRepository.save(board);
    }

    public List<Board> getAllBoard() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User foundUser = userRepository.findUserByUsername(userDetails.getUsername()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found"));
        Set<Team> teams = foundUser.getTeam();
        List<Board> allBoards = new ArrayList<>();
        for (Team team : teams) {
            allBoards.addAll(team.getBoardList());
        }
        return allBoards;
    }

    //Generez un raport in format CSV pentru un anumit board (admin, team_leader):
    //Mergem prin fiecare coloana din board
    //Numaram cate task-uri sunt in fiecare coloana din board
    //Am putea sa generam un map in care cheia e numele coloanei si valoarea e numarul de task-uri din coloana
    //De vazut daca din map-ul generat putem scrie in CSV

    /*public CsvExportService csvBoard(){

    }*/

    public ResponseEntity<String> deleteBoard(Long boardId){ //f
        Board foundBoard = boardRepository.findById(boardId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Board was not found"));
        boardRepository.delete(foundBoard);
        String responseMessage = "Board with id " + boardId + " has been successfully deleted.";
        return ResponseEntity.ok(responseMessage);
    }

}
