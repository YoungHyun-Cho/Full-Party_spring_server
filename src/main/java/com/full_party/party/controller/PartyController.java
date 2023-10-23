package com.full_party.party.controller;

import com.full_party.comment.dto.CommentReplyDto;
import com.full_party.comment.dto.CommentResponseDto;
import com.full_party.comment.mapper.CommentMapper;
import com.full_party.comment.service.CommentService;
import com.full_party.party.dto.*;
import com.full_party.party.entity.Party;
import com.full_party.party.entity.Waiter;
import com.full_party.party.mapper.PartyMapper;
import com.full_party.party.service.PartyService;
import com.full_party.tag.service.TagService;
import com.full_party.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/parties")
public class PartyController {

    private final PartyService partyService;
    private final TagService tagService;
    private final UserService userService;
    private final CommentService commentService;
    private final PartyMapper partyMapper;
    private final CommentMapper commentMapper;
    private static final String PARTY_DEFAULT_URL = "/v1/parties";

    public PartyController(PartyService partyService, TagService tagService, UserService userService, CommentService commentService, PartyMapper partyMapper, CommentMapper commentMapper) {
        this.partyService = partyService;
        this.tagService = tagService;
        this.userService = userService;
        this.commentService = commentService;
        this.partyMapper = partyMapper;
        this.commentMapper = commentMapper;
    }

    // # 기본 CRUD
    // 파티장 : 퀘스트 생성
    @PostMapping
    public ResponseEntity postParty(@RequestBody PartyPostDto partyPostDto,
                                    @AuthenticationPrincipal UserDetails userDetails) {

        Party party = partyService.createParty(
                partyMapper.partyPostDtoToParty(partyPostDto),
                userService.findUser(userDetails.getUsername())
        );

        tagService.createTagList(party, partyPostDto.getTags());

        URI uri =
                UriComponentsBuilder
                        .newInstance()
                        .path(PARTY_DEFAULT_URL + "/{party-id}")
                        .buildAndExpand(party.getId())
                        .toUri();

        return ResponseEntity.created(uri).build();
    }

    // 공통 : 내 파티 및 지역 파티 목록 조회
    @GetMapping
    public ResponseEntity getRelatedPartyList(@RequestParam(name = "region") String region,
                                              @AuthenticationPrincipal UserDetails userDetails) {

        Long userId = userService.findUser(userDetails.getUsername()).getId();
        List<PartyResponseDto> myParties = partyMapper.mapEachPartyToPartyResponseDto(partyService.findMyParties(userId));
        List<PartyResponseDto> localParties = partyMapper.mapEachPartyToPartyResponseDto(partyService.findLocalParties(userId, region));

        return new ResponseEntity(partyMapper.mapToPartyListResponseDto(myParties, localParties), HttpStatus.OK);
    }

    // 공통 : 파티 정보 조회
    @GetMapping("/{party-id}")
    public ResponseEntity getPartyInfo(@PathVariable("party-id") Long partyId) {

        Party party = partyService.findParty(partyId);
        PartyResponseDto partyResponseDto = partyMapper.partyToPartyResponseDto(party);

        commentService.findComments(partyId).stream()
                .map(comment -> commentMapper.commentToCommentResponseDto(comment))
                .forEach(commentResponseDto -> {
                    List<CommentResponseDto> replies = commentService.findReplies(commentResponseDto.getId()).stream()
                            .map(reply -> commentMapper.replyToCommentResponseDto(reply))
                            .collect(Collectors.toList());
                    partyResponseDto.getComments().add(new CommentReplyDto(commentResponseDto, replies));
                }
        );

        return new ResponseEntity(partyResponseDto, HttpStatus.OK);
    }
//
//    // 파티장 : 파티 정보 수정
//    @PatchMapping("/{quest-id}")
//    public ResponseEntity patchQuestInfo(@PathVariable("quest-id") Long questId,
//                                         @RequestBody QuestDto questDto) {
//
//        questDto.setQuestId(questId);
//        Quest updatedQuest = questService.updateQuest(questMapper.questDtoToQuest(questDto));
//
//        return new ResponseEntity(questMapper.questToQuestResponseDto(updatedQuest), HttpStatus.OK);
//    }
//
//    // 파티장 : 파티 삭제
//    @DeleteMapping("/{quest-id}")
//    public ResponseEntity deleteQuest(@PathVariable("quest-id") Long questId) {
//
//        questService.deleteQuest(questId);
//
//        return new ResponseEntity(HttpStatus.NO_CONTENT);
//    }

    // 🟥 여기까지 기존 quest controller

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
    // 파티장의 파티 정보 수정도 포함해야 함.
    @PatchMapping("/{party-id}")
    public ResponseEntity patchParty(@PathVariable("party-id") Long partyId,
                                     @RequestBody PartyPatchDto partyPatchDto) {

        partyPatchDto.setId(partyId);

        Party party = partyService.updateParty(partyMapper.partyPatchDtoToParty(partyPatchDto));

        return new ResponseEntity(partyMapper.partyToPartyResponseDto(party), HttpStatus.OK);
    }

    // 파티원 리뷰 -> 보류. 추후 구체적 기능 파악 후 구현
    @PostMapping("/{party-id}/review")
    public ResponseEntity postReview(@PathVariable("party-id") Long partyId) {

        return new ResponseEntity(HttpStatus.OK);
    }
}
