package com.full_party.mail.controller;

import com.full_party.mail.dto.MailDto;
import com.full_party.mail.service.MailService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/mails")
public class MailController {

    private final MailService mailService;

    public MailController(MailService mailService) {
        this.mailService = mailService;
    }

    @PostMapping("/verification")
    public ResponseEntity verifyMail(@RequestBody MailDto mailDto) {

        System.out.println("🟥" + mailDto.getEmail() + "🟥");

        Integer number = mailService.sendMail(mailDto.getEmail());

        return new ResponseEntity<>(new MailDto(number), HttpStatus.OK);
    }
}
