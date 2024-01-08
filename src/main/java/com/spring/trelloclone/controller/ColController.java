package com.spring.trelloclone.controller;

import com.spring.trelloclone.service.ColService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/column")
public class ColController {

    private ColService colService;

    @Autowired
    public ColController(ColService colService) {
        this.colService = colService;
    }


}
