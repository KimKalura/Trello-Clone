package com.spring.trelloclone.controller;

import com.spring.trelloclone.model.Col;
import com.spring.trelloclone.repository.BoardRepository;
import com.spring.trelloclone.service.ColService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/column")
public class ColController {

    private ColService colService;
    private BoardRepository boardRepository;


    @Autowired
    public ColController(ColService colService, BoardRepository boardRepository) {
        this.colService = colService;
        this.boardRepository = boardRepository;
    }


    @PostMapping("/create/{boardId}")
    public Col addColumnInABoard(@PathVariable Long boardId, @RequestParam String titleColumn) {
        return colService.addColumnInABoard(titleColumn, boardId);
    }

    @PostMapping("/create")
    public Col createColumn(@RequestParam String title) {
        return colService.createColumn(title);
    }

    @GetMapping("board/{boardId}")
    public List<Col> getAllColumnsFromABoard(@PathVariable Long boardId) {
        return colService.getAllColumnsFromABoard(boardId);
    }

    @DeleteMapping("/delete/{columnId}")
    public ResponseEntity<String> deleteColumnFromABoard(@PathVariable Long columnId) {
        return colService.deleteColumnFromABoard(columnId);
    }

    @GetMapping("/allColumns")
    public List<Col> getAllColumnsFromAllBoards() {
        return colService.getAllColumnsFromAllBoards();
    }


}