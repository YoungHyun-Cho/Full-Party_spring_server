package com.full_party.heart.controller;

import com.full_party.heart.entity.Heart;
import com.full_party.heart.mapper.HeartMapper;
import com.full_party.heart.service.HeartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/hearts")
public class HeartController {

    private final HeartService heartService;
    private final HeartMapper heartMapper;

    public HeartController(HeartService heartService, HeartMapper heartMapper) {
        this.heartService = heartService;
        this.heartMapper = heartMapper;
    }

    @PostMapping("/{party-id}")
    public ResponseEntity postHeart(@PathVariable("party-id") Long partyId,
                                    @RequestHeader("user-id") Long userId) {
        heartService.createHeart(userId, partyId);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity getHearts(@RequestHeader("user-id") Long userId) {

        List<Heart> heartList = heartService.findHearts(userId);

        return new ResponseEntity(heartMapper.heartListToHeartResponseDto(heartList), HttpStatus.OK);
    }

    @DeleteMapping("/{party-id}")
    public ResponseEntity deleteHeart(@PathVariable("party-id") Long partyId,
                                      @RequestHeader("user-id") Long userId) {

        heartService.deleteHeart(userId, partyId);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
