package com.full_party.mail.controller;

import com.full_party.mail.dto.MailDto;
import com.full_party.mail.service.MailService;
import com.full_party.user.service.UserService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mails")
public class MailController {

    private final MailService mailService;
    private final UserService userService;

    @PostMapping("/verification")
    public ResponseEntity verifyMail(@RequestBody MailDto mailDto) {

        userService.verifyExistsEmail(mailDto.getEmail());

        Integer number = mailService.sendMail(mailDto.getEmail());

        return new ResponseEntity<>(new MailDto(number), HttpStatus.OK);
    }
}
