package com.full_party.search.controller;

import com.full_party.party.dto.PartyResponseDto;
import com.full_party.party.entity.Party;
import com.full_party.party.mapper.PartyMapper;
import com.full_party.party.service.PartyService;
import com.full_party.search.mapper.SearchMapper;
import com.full_party.tag.entity.Tag;
import com.full_party.tag.service.TagService;
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
@RequestMapping("/v1/search")
public class SearchController {
    // # 검색 관련 기능
    // 키워드 & 태그 검색

    private final PartyService partyService;
    private final TagService tagService;
    private final UserService userService;
    private final PartyMapper partyMapper;
    private final SearchMapper searchMapper;

    public SearchController(PartyService partyService, TagService tagService, UserService userService, PartyMapper partyMapper, SearchMapper searchMapper) {
        this.partyService = partyService;
        this.tagService = tagService;
        this.userService = userService;
        this.partyMapper = partyMapper;
        this.searchMapper = searchMapper;
    }

    @GetMapping("/keyword")
    public ResponseEntity searchByKeyword(@RequestParam("value") String keyword,
                                          @RequestParam("region") String region,
                                          @AuthenticationPrincipal UserDetails userDetails) {

        User user = userService.findUser(userDetails.getUsername());

        List<PartyResponseDto> partyResponseDtos = partyService.findPartiesByKeyword(keyword, user.getId(), region).stream()
                .map(party -> partyMapper.partyToPartyResponseDto(party))
                .collect(Collectors.toList());

        return new ResponseEntity(searchMapper.mapToSearchResponseDto(partyResponseDtos), HttpStatus.OK);
    }

    @GetMapping("/tag")
    public ResponseEntity searchByTag(@RequestParam("region") String region,
                                      @RequestParam("value") String tagValue,
                                      @AuthenticationPrincipal UserDetails userDetails) {

        User user = userService.findUser(userDetails.getUsername());

        List<PartyResponseDto> partyResponseDtos = partyService.findPartiesByTag(tagValue, user.getId(), region).stream()
                .map(party -> partyMapper.partyToPartyResponseDto(party))
                .collect(Collectors.toList());

        return new ResponseEntity(searchMapper.mapToSearchResponseDto(partyResponseDtos), HttpStatus.OK);
    }
}

/*
* region 내에서 검색하도록 수정 필요
*
* //
//        List<Tag> tags = tagService.findTagsByTagValue(tagValue);
//
//        List<PartyResponseDto> partyResponseDtos = tags.stream()
//                .map(tag -> partyService.findParty(user.getId(), tag.getParty().getId()))
//                .filter(party -> party.getRegion().equals(region))
//                .map(party -> partyMapper.partyToPartyResponseDto(party))
//                .collect(Collectors.toList());
        // -> 전체 데이터를 디비에서 꺼내서 필터 -> 비효율적
        // 🟥 But, 아래는 DB단에서 필요한 데이터만 추출함. (현 searchByTag 코드)
* */