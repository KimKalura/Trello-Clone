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


    public Step checkStep(Long stepId) {
        Step foundStep = stepRepository.findById(stepId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Step was not found"));
        if(foundStep.isChecked()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Step is already checked");
        }
        foundStep.setChecked(true);
        foundStep.getTask().setCheckedDate(LocalDateTime.now());
        return stepRepository.save(foundStep);
    }

    public Step uncheckStep(Long stepId) {
        Step foundStep = stepRepository.findById(stepId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Step was not found"));
        foundStep.setChecked(false);
        foundStep.getTask().setCheckedDate(null);
        return stepRepository.save(foundStep);
    }

}
