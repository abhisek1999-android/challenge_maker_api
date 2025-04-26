package com.abhisek.challenge_maker.challenge_maker.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FunRestController {

    @GetMapping("/")
    public String Hello(){
        return "Hello";
    }
}
