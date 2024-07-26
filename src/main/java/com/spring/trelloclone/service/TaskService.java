package com.spring.trelloclone.service;

import com.itextpdf.text.DocumentException;
import com.spring.trelloclone.dto.TaskRequestDTO;
import com.spring.trelloclone.dto.TaskResponseDTO;
import com.spring.trelloclone.model.*;
import com.spring.trelloclone.repository.ColRepository;
import com.spring.trelloclone.repository.TaskHistoryRepository;
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
    private TaskHistoryRepository taskHistoryRepository;


    @Autowired
    public TaskService(TaskRepository taskRepository, UserRepository userRepository, ColRepository colRepository, MailService mailService, TaskHistoryRepository taskHistoryRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.colRepository = colRepository;
        this.mailService = mailService;
        this.taskHistoryRepository = taskHistoryRepository;
    }

    public boolean hasUserRole(User user, RoleType roleType) {
        return user.getRoleList().stream().anyMatch(role -> role.getRoleType() == roleType);
    }

    public Task createTask(TaskRequestDTO taskRequestDTO) throws MessagingException, DocumentException {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User foundUser = userRepository.findUserByUsername(userDetails.getUsername()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        User assignee;
        if ((hasUserRole(foundUser, RoleType.TEAM_LEADER)) || ((hasUserRole(foundUser, RoleType.ROLE_ADMIN)))) {
            assignee = userRepository.findById(taskRequestDTO.getAssigneeId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "The responsible user was not found."));
        } else if (hasUserRole(foundUser, RoleType.TEAM_MEMBER)) {
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
        // newTask.setCheckedDate(LocalDateTime.now()); //
        newTask.setAssigneeUser(assignee.getUsername());//userul logat sau cel din db //String.valueOf(assignee)
        newTask.setCol(foundColumn);

        List<Step> steps = new ArrayList<>();
        for (Step stepText : taskRequestDTO.getSteps()) {
            Step step = new Step();
            step.setText(stepText.getText());
            step.setChecked(false);
            step.setTask(newTask);
            steps.add(step);
        }
        newTask.setStepList(steps);

        //Trimit notificare prin mail catre user-ul asignat
        mailService.sendNotificationToAssignee(assignee.getEmail(), newTask);

        TaskHistory taskHistory = new TaskHistory();
        taskHistory.setMovementDate(newTask.getCreatedDate());
        taskHistory.setColumnName(foundColumn.getTitle());
        taskHistory.setTask(newTask);
        newTask.getTaskHistoryList().add(taskHistory);

        return taskRepository.save(newTask);
    }

    public TaskResponseDTO assignUserToTask(Long userId, Long taskId) {
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

    public List<TaskResponseDTO> getTasksByUserId(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        List<Task> tasks = taskRepository.findByAssigneeUser(user.getUsername());
        return tasks.stream()
                .map(task -> convertToDTO(task))
                .collect(Collectors.toList());
    }

    public TaskResponseDTO getTaskDetails(Long taskId) {
        Task foundTask = taskRepository.findById(taskId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task was not found"));
        return convertToDTO(foundTask);
    }

    public List<TaskResponseDTO> moveTask(Long taskId, Long columnId) {
        Task foundTask = taskRepository.findById(taskId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task was not found"));
        Col newColumn = colRepository.findById(columnId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Column not found"));
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User foundUser = userRepository.findUserByUsername(userDetails.getUsername()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (!foundUser.getRoleList().contains(RoleType.ROLE_ADMIN) &&
            !foundUser.getRoleList().contains(RoleType.TEAM_LEADER) &&
            !foundTask.getAssigneeUser().equals(foundUser.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You do not have permission to move this task");
        }

        foundTask.setCol(newColumn);

        TaskHistory taskHistory = new TaskHistory();
        taskHistory.setTask(foundTask);
        taskHistory.setMovementDate(LocalDateTime.now());
        taskHistory.setColumnName(newColumn.getTitle());

        /*if (foundTask.getTaskHistoryList() == null) {
            foundTask.setTaskHistoryList(new ArrayList<>());
        }*/
        foundTask.getTaskHistoryList().add(taskHistory);

        taskRepository.save(foundTask);
        taskHistoryRepository.save(taskHistory);

        List<Task> userTasks = taskRepository.findByAssigneeUser(foundUser.getUsername());

        return userTasks.stream()
                .map(taskItem -> convertToDTO(taskItem))
                .collect(Collectors.toList());
    }

}
