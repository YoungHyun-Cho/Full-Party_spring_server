package com.full_party.quest.controller;

import com.full_party.party.dto.PartyResponseDto;
import com.full_party.party.entity.Party;
import com.full_party.party.service.PartyService;
import com.full_party.quest.dto.QuestDto;
import com.full_party.quest.dto.QuestResponseDto;
import com.full_party.quest.entity.Quest;
import com.full_party.quest.mapper.QuestMapper;
import com.full_party.quest.service.QuestService;
import com.full_party.tag.entity.Tag;
import com.full_party.tag.service.TagService;
import com.full_party.user.entity.User;
import com.full_party.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/quests")
public class QuestController {

    private static final String QUEST_DEFAULT_URL = "v1/quests";
    private final QuestService questService;
    private final TagService tagService;
    private final PartyService partyService;
    private final QuestMapper questMapper;

    public QuestController(QuestService questService, TagService tagService, PartyService partyService, QuestMapper questMapper) {
        this.questService = questService;
        this.tagService = tagService;
        this.partyService = partyService;
        this.questMapper = questMapper;
    }

    // # 기본 CRUD
    // 파티장 : 퀘스트 생성
    @PostMapping
    public ResponseEntity postQuest(@RequestBody QuestDto questDto) {

        Quest mappedQuest = questMapper.questDtoToQuest(questDto);

        Party party = partyService.createParty(mappedQuest, questDto.getMemberLimit());

        ArrayList<Tag> tagList = tagService.createTagList(mappedQuest, questDto.getTags());

        Quest resultQuest = questService.createQuest(mappedQuest, party, tagList);

        URI uri =
                UriComponentsBuilder
                        .newInstance()
                        .path(QUEST_DEFAULT_URL + "/{quest-id}")
                        .buildAndExpand(resultQuest.getId())
                        .toUri();

        return ResponseEntity.created(uri).build();
    }

    // 공통 : 내 파티 및 지역 파티 목록 조회
    @GetMapping
    public ResponseEntity getRelatedQuestList(@RequestParam(name = "userId") Long userId,
                                              @RequestParam(name = "region") String region) {

        List<QuestResponseDto> myQuests = null; // userParty 테이블에서 userId가 일치하는 파티의 partyId와 연관된 quest 조회 -> PartyService에서 구현하고 여기에서 호출
        List<QuestResponseDto> localQuests =
                questService.findLocalQuests(region).stream()
                        .map(quest -> questMapper.questToQuestResponseDto(quest))
                        .collect(Collectors.toList());

        /*
        * partyList API를 quest가 아니라 party Controller에서 받아와서 처리하는 것이 나아 보임.
        * 또는.. quest랑 party를 DB 상에서 병합해버리는 것도 고려해볼만 함. -> 이게 나아보인다..
        * 굳이 quest랑 party를 구분할 필요가...
        *
        * 필요 작업
        * 1. 엔티티 클래스 수정
        *   - Quest를 참조하는 다른 모든 엔티티 수정 필요
        *   - MySql 삭제 후 재생성 필요
        * 2. Quest 패키지 컴포넌트를 모두 Party로 변환 및 이동
        *   - Controller api 고려 필요
        *
        * */

        return new ResponseEntity(questMapper.mapToQuestListResponseDto(myQuests, localQuests), HttpStatus.OK);
    }

    // 공통 : 파티 정보 조회
    @GetMapping("/{quest-id}")
    public ResponseEntity getQuestInfo(@PathVariable("quest-id") Long questId) {

        Quest quest = questService.findQuest(questId);

        return new ResponseEntity(questMapper.questToQuestResponseDto(quest), HttpStatus.OK);
    }

    // 파티장 : 파티 정보 수정
    @PatchMapping("/{quest-id}")
    public ResponseEntity patchQuestInfo(@PathVariable("quest-id") Long questId,
                                         @RequestBody QuestDto questDto) {

        questDto.setQuestId(questId);
        Quest updatedQuest = questService.updateQuest(questMapper.questDtoToQuest(questDto));

        return new ResponseEntity(questMapper.questToQuestResponseDto(updatedQuest), HttpStatus.OK);
    }

    // 파티장 : 파티 삭제
    @DeleteMapping("/{quest-id}")
    public ResponseEntity deleteQuest(@PathVariable("quest-id") Long questId) {

        questService.deleteQuest(questId);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
