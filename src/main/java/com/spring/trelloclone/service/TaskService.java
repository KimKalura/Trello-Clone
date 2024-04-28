package com.spring.trelloclone.service;

import com.itextpdf.text.DocumentException;
import com.spring.trelloclone.dto.TaskRequestDTO;
import com.spring.trelloclone.model.*;
import com.spring.trelloclone.repository.ColRepository;
import com.spring.trelloclone.repository.TaskRepository;
import com.spring.trelloclone.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.mail.MessagingException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class TaskService {

    private TaskRepository taskRepository;
    private UserRepository userRepository;
    private ColRepository colRepository;
    private MailService mailService;


    @Autowired
    public TaskService(TaskRepository taskRepository, UserRepository userRepository, ColRepository colRepository, MailService mailService) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.colRepository = colRepository;
        this.mailService = mailService;
    }

    //Creez un nou task intr-o anumita coloana (admin, team_leader, team_member)
    public Task createTask(TaskRequestDTO taskRequestDTO) throws MessagingException, DocumentException {//
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User foundUser = userRepository.findUserByUsername(userDetails.getUsername()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        User assignee;
        if (foundUser.getRoleList().contains(RoleType.TEAM_LEADER)|| foundUser.getRoleList().contains(RoleType.ROLE_ADMIN)) {

            assignee = userRepository.findById(taskRequestDTO.getAssigneeId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "The responsible user was not found."));
        } else if (foundUser.getRoleList().contains(RoleType.TEAM_MEMBER)) {
            assignee = foundUser;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        Col foundColumn = colRepository.findById(taskRequestDTO.getColumnId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Column was not found"));

        Task newTask = new Task();
        newTask.setTitle(taskRequestDTO.getTitle());
        newTask.setDescription(taskRequestDTO.getDescription());
        newTask.setDeadline(taskRequestDTO.getDeadline());
        newTask.setCreatedDate(LocalDateTime.now());
        newTask.setAssigneeUser(String.valueOf(assignee));//userul logat sau cel din db
        newTask.setCol(foundColumn);

        List<Step> steps = new ArrayList<>(); //taskRequestDTO.getSteps();
        for (Step stepText : taskRequestDTO.getSteps()) {
            Step step = new Step();
            step.setText(stepText.getText()); //*stepText
            step.setChecked(false);
            step.setTask(newTask);
            //newTask.getStepList().add(step);
            steps.add(step);
        }
        newTask.setStepList(steps);

        //Trimit notificare prin mail catre user-ul asignat
        mailService.sendNotificationToAssignee(assignee.getEmail(), newTask);


        TaskHistory taskHistory = new TaskHistory();
        taskHistory.setMovementDate(newTask.getCreatedDate());
        taskHistory.setColumnName(foundColumn.getTitle());// getname
        newTask.getTaskHistoryList().add(taskHistory);

        return taskRepository.save(newTask);
    }
}
