package com.full_party.domain.search.controller;

import com.full_party.domain.party.dto.PartyResponseDto;
import com.full_party.domain.party.mapper.PartyMapper;
import com.full_party.domain.party.service.PartyService;
import com.full_party.domain.search.mapper.SearchMapper;
import com.full_party.domain.user.entity.User;
import com.full_party.domain.user.service.UserService;
import com.full_party.domain.notification.service.NotificationService;
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
@RequestMapping("/search")
public class SearchController {

    private final PartyService partyService;
    private final UserService userService;
    private final NotificationService notificationService;
    private final PartyMapper partyMapper;
    private final SearchMapper searchMapper;

    @GetMapping("/keyword")
    public ResponseEntity searchByKeyword(@RequestParam("value") String keyword,
                                          @RequestParam("region") String region,
                                          @AuthenticationPrincipal UserDetails userDetails) {

        User user = userService.findUser(userDetails.getUsername());

        List<PartyResponseDto> partyResponseDtos = partyService.findPartiesByKeyword(keyword, user.getId(), region).stream()
                .map(party -> partyMapper.partyToPartyResponseDto(party))
                .collect(Collectors.toList());

        return new ResponseEntity(
                searchMapper.mapToSearchResponseDto(
                        notificationService.checkNotificationBadge(user.getId()),
                        partyResponseDtos,
                        user.getCoordinates()
                ),
                HttpStatus.OK
        );
    }

    @GetMapping("/tag")
    public ResponseEntity searchByTag(@RequestParam("value") String tagValue,
                                      @RequestParam("region") String region,
                                      @AuthenticationPrincipal UserDetails userDetails) {

        User user = userService.findUser(userDetails.getUsername());

        System.out.println(partyService.findParty(1L).getRegion().equals(region));

        List<PartyResponseDto> partyResponseDtos = partyService.findPartiesByTag(tagValue, user.getId(), region).stream()
                .map(party -> partyMapper.partyToPartyResponseDto(party))
                .collect(Collectors.toList());

        return new ResponseEntity(
                searchMapper.mapToSearchResponseDto(
                        notificationService.checkNotificationBadge(user.getId()),
                        partyResponseDtos,
                        user.getCoordinates()
                ),
                HttpStatus.OK
        );
    }
}
