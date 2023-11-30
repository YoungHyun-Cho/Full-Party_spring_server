package com.full_party.heart.controller;

import com.full_party.heart.entity.Heart;
import com.full_party.heart.mapper.HeartMapper;
import com.full_party.heart.service.HeartService;
import com.full_party.notification.service.NotificationService;
import com.full_party.party.dto.PartyResponseDto;
import com.full_party.party.mapper.PartyMapper;
import com.full_party.party.service.PartyService;
import com.full_party.user.entity.User;
import com.full_party.user.service.UserService;
import com.full_party.values.PartyState;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/hearts")
public class HeartController {

    private final HeartService heartService;
    private final UserService userService;
    private final PartyService partyService;
    private final NotificationService notificationService;
    private final HeartMapper heartMapper;
    private final PartyMapper partyMapper;

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
                .filter(party -> party.getPartyState() != PartyState.DISMISSED)
                .map(party -> partyMapper.partyToPartyResponseDto(party))
                .collect(Collectors.toList());

        return new ResponseEntity(
                heartMapper.partyListToHeartResponseDto(
                        notificationService.checkNotificationBadge(user.getId()), parties
                ),
                HttpStatus.OK
        );
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
