package com.spring.trelloclone.controller;

import com.spring.trelloclone.service.CsvExportService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/csv")
public class CsvExportController {
    CsvExportService csvExportService;

    @Autowired
    public CsvExportController(CsvExportService csvExportService) {
        this.csvExportService = csvExportService;
    }

    //1
    @RequestMapping(path = "/csvboards")
    public void getAllBoardsInCsv(HttpServletResponse servletResponse) throws IOException {
        servletResponse.setContentType("text/csv");
        servletResponse.addHeader("Content-Disposition", "attachment; filename=\"boards.csv\"");
        csvExportService.getAllBoardsInCsv(servletResponse.getWriter());
    }

    //2
    @GetMapping("/report/{id}")
    public ResponseEntity<String> generateReportForBoard(@PathVariable Long id) {
        Map<String, Integer> reportData = csvExportService.generateReportForBoard(id);
        StringBuilder csvContent = new StringBuilder();
        csvContent.append("ID, Title, Description, Created Date, Column List\n");
        for (Map.Entry<String, Integer> entry : reportData.entrySet()) {
            csvContent.append(entry.getKey())
                    .append(",")
                    .append(entry.getValue())
                    .append("\n");
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("text/csv"));
        headers.setContentDispositionFormData("filename", "board_report.csv");
        return new ResponseEntity<>(csvContent.toString(), headers, HttpStatus.OK);
    }


}
