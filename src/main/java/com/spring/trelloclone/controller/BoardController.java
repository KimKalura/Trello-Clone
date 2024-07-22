package com.spring.trelloclone.controller;

import com.spring.trelloclone.model.Board;
import com.spring.trelloclone.service.BoardService;
import com.spring.trelloclone.service.CsvExportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/board")
public class BoardController {

    private BoardService boardService;

    private final CsvExportService csvExportService;

    @Autowired
    public BoardController(BoardService boardService, CsvExportService csvExportService) {
        this.boardService = boardService;
        this.csvExportService = csvExportService;
    }


    @PostMapping("/create")
    public Board createCategory(@RequestBody Board board, @RequestParam Long teamId) {
        return boardService.addBoard(board, teamId);
    }

    @GetMapping("/myBoards")
    public List<Board> getAllBoard() {
        return boardService.getAllBoard();
    }

    @DeleteMapping("/delete/{boardId}")
    public ResponseEntity<String> deleteBoard(@PathVariable Long boardId){
        return boardService.deleteBoard(boardId);
    }

}
