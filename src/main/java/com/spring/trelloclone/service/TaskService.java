package com.spring.trelloclone.service;

import com.itextpdf.text.DocumentException;
import com.spring.trelloclone.dto.TaskRequestDTO;
import com.spring.trelloclone.dto.TaskResponseDTO;
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
import java.util.stream.Collectors;

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
        if (foundUser.getRoleList().contains(RoleType.TEAM_LEADER) || foundUser.getRoleList().contains(RoleType.ROLE_ADMIN)) {

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
        //mailService.sendNotificationToAssignee(assignee.getEmail(), newTask);


        TaskHistory taskHistory = new TaskHistory();
        taskHistory.setMovementDate(newTask.getCreatedDate());
        taskHistory.setColumnName(foundColumn.getTitle());// getname
        newTask.getTaskHistoryList().add(taskHistory);

        return taskRepository.save(newTask);
    }

    public TaskResponseDTO  assignUserToTask(Long userId, Long taskId) { ////////*
    //Cautam task-ul dupa id, daca nu il gasim aruncam exceptie
    //Cautam user-ul dupa id, daca nu il gasim aruncam exceptie
    //Ii setam task-ului user-ul gasit in db
    //Trimit notificare prin mail catre user-ul asignat
    //Salvam task-ul

        Task foundTask = taskRepository.findById(taskId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task was not found"));
        User user = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        foundTask.setAssigneeUser(user.getUsername());
        Task updatedTask = taskRepository.save(foundTask);
        mailService.sendAssignmentNotification(user.getEmail(), foundTask);
        return convertToDTO(updatedTask);
    }

    private TaskResponseDTO convertToDTO(Task task) {
        User assignee = new User();
        assignee.setUsername(task.getAssigneeUser());

        TaskResponseDTO taskResponseDTO = new TaskResponseDTO();
        taskResponseDTO.setId(task.getId());
        taskResponseDTO.setTitle(task.getTitle());
        taskResponseDTO.setDescription(task.getDescription());
        taskResponseDTO.setAssigneeUser(assignee);
        taskResponseDTO.setCreatedDate(task.getCreatedDate());
        taskResponseDTO.setDeadline(task.getDeadline());
        taskResponseDTO.setChecklist(task.getStepList());
        taskResponseDTO.setTaskColumn(task.getCol());
        taskResponseDTO.setTaskBoard(task.getCol().getBoard());
        return taskResponseDTO;
    }

}
