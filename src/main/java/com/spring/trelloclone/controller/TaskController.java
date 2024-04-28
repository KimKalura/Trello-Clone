package com.spring.trelloclone.controller;

import com.itextpdf.text.DocumentException;
import com.spring.trelloclone.dto.TaskRequestDTO;
import com.spring.trelloclone.model.Task;
import com.spring.trelloclone.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;

@RestController
@RequestMapping("/task")
public class TaskController {

    private TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/create")//
    public Task createTask(@RequestBody TaskRequestDTO taskRequestDTO, @RequestParam(required = false) Long assigneeId) throws MessagingException, DocumentException {
        if (assigneeId != null) {
            taskRequestDTO.setAssigneeId(assigneeId);
        }
        return taskService.createTask(taskRequestDTO);
    }
}
