package com.full_party.quest.controller;

import com.full_party.quest.dto.QuestPatchDto;
import com.full_party.quest.dto.QuestPostDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/quests")
public class QuestController {

    // # 기본 CRUD
    // 파티장 : 퀘스트 생성
    @PostMapping
    public ResponseEntity postQuest(@RequestBody QuestPostDto partyPostDto) {
        return new ResponseEntity(partyPostDto, HttpStatus.CREATED);
    }

    // 공통 : 내 파티 및 지역 파티 목록 조회
    @GetMapping
    public ResponseEntity getRelatedPartyList(@RequestParam(name = "userId") Long userId,
                                              @RequestParam(name = "region") String region) {
        return new ResponseEntity(HttpStatus.OK);
    }

    // 공통 : 파티 정보 조회
    @GetMapping("/{quest-id}")
    public ResponseEntity getPartyInfo(@PathVariable("quest-id") Long questId) {
        return new ResponseEntity(HttpStatus.OK);
    }

    // 파티장 : 파티 정보 수정
    @PatchMapping("/{quest-id}")
    public ResponseEntity patchPartyInfo(@PathVariable("quest-id") Long questId,
                                         @RequestBody QuestPatchDto partyPatchDto) {

        partyPatchDto.setQuestId(questId);

        return new ResponseEntity(partyPatchDto, HttpStatus.OK);
    }

    // 파티장 : 파티 삭제
    @DeleteMapping("/{quest-id}")
    public ResponseEntity deleteParty(@PathVariable("quest-id") Long questId) {
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
