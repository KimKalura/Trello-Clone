package com.spring.trelloclone.controller;

import com.spring.trelloclone.service.StepService;
import org.springframework.beans.factory.annotation.Autowired;
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
}
