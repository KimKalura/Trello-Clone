package com.spring.trelloclone.controller;

import com.spring.trelloclone.model.Step;
import com.spring.trelloclone.service.StepService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/step")
public class StepController {

    private StepService stepService;

    @Autowired
    public StepController(StepService stepService) {
        this.stepService = stepService;
    }

    @PutMapping("/check/{stepId}")
    public Step checkStep(@PathVariable Long stepId) {
        return stepService.checkStep(stepId);
    }

    @PutMapping("/uncheck/{stepId}")
    public Step uncheckStep(@PathVariable Long stepId) {
        return stepService.uncheckStep(stepId);
    }

}
