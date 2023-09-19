package com.full_party.user.controller;

import com.full_party.user.dto.UserPatchDto;
import com.full_party.user.dto.UserPostDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/users")
public class UserController {

    @PostMapping
    public ResponseEntity postUser(@RequestBody UserPostDto userPostDto) {
        return new ResponseEntity(userPostDto, HttpStatus.CREATED);
    }

    @GetMapping("/{user-id}")
    public ResponseEntity getUser(@PathVariable("user-id") Long userId) {
        return new ResponseEntity(HttpStatus.OK);
    }

    @PatchMapping("/{user-id}")
    public ResponseEntity patchUser(@PathVariable("user-id") Long userId, @RequestBody UserPatchDto userPatchDto) {

        userPatchDto.setUserId(userId);

        return new ResponseEntity(userPatchDto, HttpStatus.OK);
    }

    @DeleteMapping("/{user-id}")
    public ResponseEntity deleteUser(@PathVariable("user-id") Long userId) {
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
