package com.full_party.quest.controller;

import com.full_party.party.entity.Party;
import com.full_party.party.service.PartyService;
import com.full_party.quest.dto.QuestDto;
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
        return new ResponseEntity(HttpStatus.OK);
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
