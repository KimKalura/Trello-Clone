package com.spring.trelloclone.service;

import com.spring.trelloclone.model.Board;
import com.spring.trelloclone.model.Step;
import com.spring.trelloclone.repository.StepRepository;
import com.spring.trelloclone.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Service
public class StepService {

    private StepRepository stepRepository;


    @Autowired
    public StepService(StepRepository stepRepository) {
        this.stepRepository = stepRepository;
    }

    //Vreau sa bifez un pas dintr-un task:
        //Cautam pasul dupa id in db, daca nu exista, aruncam exceptie
        //Ii setam checked pe true
        //Ii punem data bifarii la data actuala (now)
        //Salvam pasul din DB
    public Step checkStep(Long stepId) {
        Step foundStep = stepRepository.findById(stepId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Step was not found"));
        foundStep.setChecked(true);
        foundStep.getTask().setCheckedDate(LocalDateTime.now());
        return stepRepository.save(foundStep);
    }

    //Vreau sa debifez un pas dintr-un task:
        //Cautam pasul dupa id in db, daca nu exista, aruncam exceptie
        //Ii setam checked pe false
        //Ii punem data bifarii la null
        //Salvam pasul din DB
    public Step uncheckStep(Long stepId) {
        Step foundStep = stepRepository.findById(stepId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Step was not found"));
        foundStep.setChecked(false);
        foundStep.getTask().setCheckedDate(null);
        return stepRepository.save(foundStep);
    }

}
