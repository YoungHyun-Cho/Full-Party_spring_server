package com.full_party.party.controller;

import com.full_party.party.dto.WaiterDto;
import com.full_party.party.dto.PartyApproveDto;
import com.full_party.party.dto.PartyPatchDto;
import com.full_party.party.entity.Party;
import com.full_party.party.entity.Waiter;
import com.full_party.party.mapper.PartyMapper;
import com.full_party.party.service.PartyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/parties")
public class PartyController {

    private final PartyService partyService;
    private final PartyMapper partyMapper;

    public PartyController(PartyService partyService, PartyMapper partyMapper) {
        this.partyService = partyService;
        this.partyMapper = partyMapper;
    }

    // 참여 신청
//    @PostMapping("/{party-id}/application")
//    public ResponseEntity applyParty(@PathVariable("party-id") Long partyId,
//                                     @RequestBody WaiterDto waiterDto) {
//
//        partyService.createWaiter(partyMapper.waiterDtoToWaiter(waiterDto));
//
//        return new ResponseEntity(HttpStatus.CREATED);
//    }

    @PostMapping("/{party-id}/application/users/{user-id}")
    public ResponseEntity applyParty(@PathVariable("party-id") Long partyId,
                                     @PathVariable("user-id") Long userId,
                                     @RequestBody WaiterDto waiterDto) {

        partyService.createWaiter(userId, partyId, waiterDto.getMessage());

        return new ResponseEntity(HttpStatus.CREATED);
    }

    // 참여 메세지 수정
    @PatchMapping("/{party-id}/message/users/{user-id}")
    public ResponseEntity patchApplyMessage(@PathVariable("party-id") Long partyId,
                                            @PathVariable("user-id") Long userId,
                                            @RequestBody WaiterDto waiterDto) {

        Waiter waiter = partyService.updateWaiterMessage(userId, partyId, waiterDto.getMessage());

        return new ResponseEntity(HttpStatus.OK);

//        Waiter waiter = partyService.updateWaiterMessage(partyMapper.waiterDtoToWaiter(waiterDto));
//
//        return new ResponseEntity(partyMapper.waiterToWaiterDto(waiter), HttpStatus.OK);
    }

    // 파티 참여 승인 🟥 Header userId -> 파티장 본인 -> RequestBody의 userId가 승인 대상임.
    @PostMapping("/{party-id}/participation/users/{user-id}")
    public ResponseEntity approveUser(@PathVariable("party-id") Long partyId,
                                      @PathVariable("user-id") Long userId,
                                      @RequestBody PartyApproveDto partyApproveDto) {

//        partyService.createUserParty(partyApproveDto.getUserId(), partyApproveDto.getPartyId());

        partyService.createUserParty(partyApproveDto.getUserId(), partyId);

        return new ResponseEntity(HttpStatus.CREATED);
    }

    // 참여 신청 취소 및 거절 🟥 Header userId -> 파티장 본인일 수도 있고, 파티원일 수도 있음.
    @DeleteMapping("/{party-id}/application/users/{user-id}")
    public ResponseEntity cancelApplication(@PathVariable("party-id") Long partyId,
                                            @PathVariable("user-id") Long userId,
                                            @RequestParam(name = "action") String action) {

        partyService.deleteWaiter(userId, partyId);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    // 파티 탈퇴 및 강퇴 🟥 Header userId -> 파티장 본인일 수도 있고, 파티원일 수도 있음.
    @DeleteMapping("/{party-id}/participation/users/{user-id}")
    public ResponseEntity withdrawParty(@PathVariable("party-id") Long partyId,
                                        @PathVariable("user-id") Long userId,
                                        @RequestParam(name = "action") String action) {

        partyService.deleteUserParty(userId, partyId);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    // 파티 상태 변경(모집 완료 / 파티 완료 / 재모집) & 멤버 리밋 변경
    @PatchMapping("/{party-id}")
    public ResponseEntity patchParty(@PathVariable("party-id") Long partyId,
                                     @RequestBody PartyPatchDto partyPatchDto) {

        partyPatchDto.setId(partyId);

        Party party = partyService.updateParty(partyMapper.partyPatchDtoToParty(partyPatchDto));

        return new ResponseEntity(partyMapper.partyToPartyResponseDto(party), HttpStatus.OK);
    }

    // 파티원 리뷰
    @PostMapping("/{party-id}/review")
    public ResponseEntity postReview(@PathVariable("party-id") Long partyId) {

        return new ResponseEntity(HttpStatus.OK);
    }
}
