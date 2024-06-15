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

    public TaskResponseDTO assignUserToTask(Long userId, Long taskId) { ////////*
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

    //Vad toate task-urile la care un user asignat (admin, team_leader, team_member)
    public List<TaskResponseDTO> getTasksByUserId(Long userId) {///*
        User user = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        List<Task> tasks = taskRepository.findByAssigneeUser(user.getUsername());
        return tasks.stream()
                .map(task -> convertToDTO(task)) //this::convertToDTO
                .collect(Collectors.toList());
    }

    //Vad detaliile unui anumit task (admin, team_leader, team_member)
    public TaskResponseDTO getTaskDetails(Long taskId) {///*
        Task foundTask = taskRepository.findById(taskId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task was not found"));
        return convertToDTO(foundTask);
    }

    //Mut task-ul din coloana curenta in alta coloana (admin, team_leader, team_member)
        //Team leader-ul va putea muta orice task
        //Team member-ul va putea muta doar task-urile la care e asignat
    //pasi:
    //Cautam task-ul dupa id in db, daca nu exista, aruncam exceptie
    //Cautam coloana dupa id in db, daca nu exista, aruncam exceptie
    //Adaug task-ul gasit in lista de task-uri a coloanei gasite (cea in care vrem sa mutam)
    //Stergem task-ul din lista de task-uri a coloanei curente in care se afla
    //Salvez in DB coloana curenta
    //Salvez in DB coloana in care mut

    //ALTERNATIVA:
    //Ii setez task-ului noua coloana
    //Creez un TaskHistory
        //Ii setez movementDate la data actuala (now)
        //Ii setez numele coloanei la numele coloanei gasite din DB
    //Salvez task-ul (de vazut daca trebuie cascade pe task, spre coloana)
    public List<TaskResponseDTO> moveTask(Long taskId, Long columnId){
        Task foundTask = taskRepository.findById(taskId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task was not found"));
        Col newColumn  = colRepository.findById(columnId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Column not found"));
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User foundUser = userRepository.findUserByUsername(userDetails.getUsername()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (!foundUser.getRoleList().contains(RoleType.ROLE_ADMIN) && !foundUser.getRoleList().contains(RoleType.TEAM_LEADER) && !foundTask.getAssigneeUser().equals(foundUser.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You do not have permission to move this task");
        }

        foundTask.setCol(newColumn);

        TaskHistory taskHistory = new TaskHistory();
        taskHistory.setTask(foundTask);
        taskHistory.setMovementDate(LocalDateTime.now());
        taskHistory.setColumnName(newColumn.getTitle());

        foundTask.getTaskHistoryList().add(taskHistory);

        taskRepository.save(foundTask);
        taskHistoryRepository.save(taskHistory);

        List<Task> userTasks = taskRepository.findByAssigneeUser(foundUser.getUsername());

        return userTasks.stream()
                .map(taskItem -> convertToDTO(taskItem))
                .collect(Collectors.toList());
    }


}
