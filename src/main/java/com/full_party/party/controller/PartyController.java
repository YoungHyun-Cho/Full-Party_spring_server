package com.full_party.party.controller;

import com.full_party.party.dto.PartyApplyDto;
import com.full_party.party.dto.PartyApproveDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/parties")
public class PartyController {

    // 참여 신청
    @PostMapping("/{party-id}/application")
    public ResponseEntity applyParty(@PathVariable("party-id") Long partyId,
                                     @RequestBody PartyApplyDto partyApplyDto) {

        return new ResponseEntity(partyApplyDto, HttpStatus.CREATED);
    }

    // 참여 메세지 수정
    @PatchMapping("/{party-id}/message")
    public ResponseEntity patchApplyMessage(@PathVariable("party-id") Long partyId,
                                            @RequestBody PartyApplyDto partyApplyDto) {

        return new ResponseEntity(partyApplyDto, HttpStatus.OK);
    }

    // 파티 참여 승인
    @PostMapping("/{party-id}/approval")
    public ResponseEntity approveUser(@PathVariable("party-id") Long partyId,
                                      @RequestBody PartyApproveDto partyApproveDto) {
        return new ResponseEntity(partyApproveDto, HttpStatus.CREATED);
    }

    // 참여 신청 취소 및 거절
    @DeleteMapping("/{party-id}/cancellation")
    public ResponseEntity cancelApplication(@PathVariable("party-id") Long partyId,
                                            @RequestParam(name = "action") String action) {
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }


    // 파티 탈퇴 및 강퇴
    @DeleteMapping("/{party-id}/withdrawal")
    public ResponseEntity withdrawParty(@PathVariable("party-id") Long partyId,
                                        @RequestParam(name = "action") String action) {
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    // 파티 상태 변경 - 모집 완료 / 파티 완료 / 재모집
    @PatchMapping("/{party-id}")
    public ResponseEntity patchPartyState(@PathVariable("party-id") Long partyId,
                                          @RequestParam(name = "state") String state) {
        return new ResponseEntity(HttpStatus.OK);
    }

    // 파티원 리뷰
    @PostMapping("/{party-id}/review")
    public ResponseEntity postReview(@PathVariable("party-id") Long partyId) {

        return new ResponseEntity(HttpStatus.OK);
    }
}
