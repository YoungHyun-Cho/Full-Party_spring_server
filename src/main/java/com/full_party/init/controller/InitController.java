package com.full_party.init.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InitController {

    @GetMapping
    public ResponseEntity root() {
        return new ResponseEntity("Hello, FullParty", HttpStatus.OK);
    }
}
