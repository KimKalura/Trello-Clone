package com.spring.trelloclone.service;

import com.spring.trelloclone.model.Board;
import com.spring.trelloclone.model.Col;
import com.spring.trelloclone.repository.BoardRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.hibernate.mapping.Column;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class CsvExportService {
    private static final Logger log = getLogger(CsvExportService.class);

    private final BoardRepository boardRepository;


    @Autowired
    public CsvExportService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    //1
    //todo: afiseaza toate board-urile:
    public void getAllBoardsInCsv(Writer writer) {

        List<Board> boards = boardRepository.findAll();

        try (CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT)) {
            csvPrinter.printRecord("ID", "Title", "Description", "Created Date", "Column List");
            //csvPrinter.printRecord("ID".trim(), "Title".trim(), "Description".trim(), "Created Date".trim(), "Column List".trim());
            for (Board board : boards) {
                csvPrinter.printRecord(board.getId(), board.getTitle(), board.getDescription(), board.getCreatedDate(), board.getColumnList());
            }
        } catch (IOException e) {
            log.error("Error While writing CSV ", e);
        }
    }


    //2
    //!de verificat
    public Map<String, Integer> generateReportForBoard(Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Board was not found."));
        Map<String, Integer> columnTasksCount = new HashMap<>();
        for (Col column : board.getColumnList()) {
            int tasksCount = column.getTaskList().size();
            columnTasksCount.put(column.getTitle(), tasksCount);
        }

        return columnTasksCount;
    }


}
