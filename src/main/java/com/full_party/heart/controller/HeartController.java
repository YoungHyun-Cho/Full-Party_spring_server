package com.full_party.heart.controller;

import com.full_party.heart.entity.Heart;
import com.full_party.heart.mapper.HeartMapper;
import com.full_party.heart.service.HeartService;
import com.full_party.party.dto.PartyResponseDto;
import com.full_party.party.mapper.PartyMapper;
import com.full_party.party.service.PartyService;
import com.full_party.user.entity.User;
import com.full_party.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/hearts")
public class HeartController {

    private final HeartService heartService;
    private final UserService userService;
    private final PartyService partyService;
    private final HeartMapper heartMapper;
    private final PartyMapper partyMapper;

    public HeartController(HeartService heartService, UserService userService, PartyService partyService, HeartMapper heartMapper, PartyMapper partyMapper) {
        this.heartService = heartService;
        this.userService = userService;
        this.partyService = partyService;
        this.heartMapper = heartMapper;
        this.partyMapper = partyMapper;
    }

    @PostMapping("/{party-id}")
    public ResponseEntity postHeart(@PathVariable("party-id") Long partyId,
                                    @AuthenticationPrincipal UserDetails userDetails) {

        heartService.createHeart(
                userService.findUser(userService.findUser(userDetails.getUsername()).getId()),
                partyService.findParty(partyId)
        );

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity getHearts(@AuthenticationPrincipal UserDetails userDetails) {

        User user = userService.findUser(userDetails.getUsername());
        List<Heart> heartList = heartService.findHearts(user);

        List<PartyResponseDto> parties = heartList.stream()
                .map(heart -> partyService.findParty(user.getId(), heart.getParty().getId()))
                .map(party -> partyMapper.partyToPartyResponseDto(party))
                .collect(Collectors.toList());

        return new ResponseEntity(heartMapper.partyListToHeartResponseDto(parties), HttpStatus.OK);
    }

    @DeleteMapping("/{party-id}")
    public ResponseEntity deleteHeart(@PathVariable("party-id") Long partyId,
                                      @AuthenticationPrincipal UserDetails userDetails) {

        heartService.deleteHeart(
                userService.findUser(userService.findUser(userDetails.getUsername()).getId()),
                partyService.findParty(partyId)
        );

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
