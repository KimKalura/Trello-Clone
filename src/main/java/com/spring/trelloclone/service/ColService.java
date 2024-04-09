package com.spring.trelloclone.service;

import com.spring.trelloclone.model.Board;
import com.spring.trelloclone.model.Col;
import com.spring.trelloclone.repository.BoardRepository;
import com.spring.trelloclone.repository.ColRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDateTime;

import java.util.List;

@Service
public class ColService {

    private ColRepository colRepository;
    private BoardRepository boardRepository;

    @Autowired
    public ColService(ColRepository colRepository, BoardRepository boardRepository) {
        this.colRepository = colRepository;
        this.boardRepository = boardRepository;
    }


    // >>Creez o noua coloana intr-un anumit board (admin, team_leader)
    //Facem o noua coloana
    //Ii setam titlul la cel pe care il luam din URL
    //Ii setam data crearii la acum
    //Salvam coloana

    //varianta 1, f
    public Col addColumnInABoard(String titleColumn, Long boardId) {
        Col columnInBoard = new Col();
        Board foundBoard = boardRepository.findById(boardId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Board not found"));
        columnInBoard.setTitle(titleColumn);
        columnInBoard.setCreatedDate(LocalDateTime.now());
        columnInBoard.setBoard(foundBoard);
        foundBoard.getColumnList().add(columnInBoard);
        boardRepository.save(foundBoard);
        return colRepository.save(columnInBoard);
    }

    //varianta 2, f
    public Col createColumn(String title) {
        Col column = new Col();
        column.setTitle(title);
        column.setCreatedDate(LocalDateTime.now());
        return colRepository.save(column);
    }


    //>>Vad coloanele dintr-un anumit board (admin, team_leader, team_member)
    //Verific daca exista board-ul cu id-ul care vine prin URL, daca nu arunc exceptie
    //Caut in DB coloanele dupa board
    //Returnez coloanele gasite
    //Daca nu gasesc nicio coloana in board, arunc o exceptie cu mesajul “no columns available in board”

    public List<Col> getAllColumnsFromABoard(Long boardId) {
        Board foundBoard = boardRepository.findById(boardId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Board not found"));
        List<Col> columns = colRepository.findByBoard(foundBoard);
        if (columns.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No columns available in board");
        }
        return columns;
    }

    //Sterg o anumita coloana dintr-un board (admin, team_leader)
    public ResponseEntity<String> deleteColumnFromABoard(Long columnId){
        Col foundColumn = colRepository.findById(columnId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Column not found"));
        colRepository.delete(foundColumn);

        String responseMessage = "Column with id " + columnId + " has been successfully deleted.";
        return ResponseEntity.ok(responseMessage);
    }

    //toate coloanele din toate boards
    public List<Col> getAllColumnsFromAllBoards(){
        List<Col> allColumns = colRepository.findAll();
        return allColumns;
    }


}
