package com.full_party.party.controller;

import com.full_party.party.dto.PartyPatchDto;
import com.full_party.party.dto.PartyPostDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/parties")
public class PartyController {

    // # 기본 CRUD
    // 파티 생성
    @PostMapping
    public ResponseEntity postParty(@RequestBody PartyPostDto partyPostDto) {
        return new ResponseEntity(partyPostDto, HttpStatus.CREATED);
    }

    // 내 파티 및 지역 파티 목록 조회
    @GetMapping("/{user-id}/{region}")
    public ResponseEntity getRelatedPartyList(@PathVariable("user-id") Long userId,
                                            @PathVariable("region") String region) {
        return new ResponseEntity(HttpStatus.OK);
    }

    // 파티 정보 조회
    @GetMapping("/{party-id}")
    public ResponseEntity getPartyInfo(@PathVariable("party-id") Long partyId) {
        return new ResponseEntity(HttpStatus.OK);
    }

    // 파티 정보 수정
    @PatchMapping("/{party-id}")
    public ResponseEntity patchPartyInfo(@PathVariable("party-id") Long partyId,
                                         @RequestBody PartyPatchDto partyPatchDto) {

        partyPatchDto.setPartyId(partyId);

        return new ResponseEntity(partyPatchDto, HttpStatus.OK);
    }

    // 파티 삭제
    @DeleteMapping("/{party-id}/{user-id}")
    public ResponseEntity deleteParty(@PathVariable("party-id") Long partyId,
                                      @PathVariable("user-id") Long userId) {
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }


    // # 심화 기능
    // 참여 신청

    // 참여 메세지 수정

    // 파티 참여 승인

    // 참여 신청 취소 및 거절

    // 파티 탈퇴 및 강퇴

    // 파티 상태 변경 - 모집 완료 / 파티 완료 / 재모집

    // 리뷰 등록

    // # 댓글 관련 기능
    // 댓글 등록

    // 대댓글 등록

    // 댓글 수정

    // 대댓글 수정

    // 댓글 삭제

    // 대댓글 삭제

    // # 검색 관련 기능
    // 키워드 검색
}
