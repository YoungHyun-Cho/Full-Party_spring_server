package com.full_party.init;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health-check")
public class InitController {

    @GetMapping
    public ResponseEntity healthCheck() {
        return new ResponseEntity(HttpStatus.OK);
    }
}
