package com.full_party.party.controller;

import com.full_party.comment.dto.CommentReplyDto;
import com.full_party.comment.dto.CommentResponseDto;
import com.full_party.comment.mapper.CommentMapper;
import com.full_party.comment.service.CommentService;
import com.full_party.exception.BusinessLogicException;
import com.full_party.exception.ExceptionCode;
import com.full_party.notification.entity.Notification;
import com.full_party.notification.service.NotificationService;
import com.full_party.party.dto.*;
import com.full_party.party.entity.Party;
import com.full_party.party.mapper.PartyMapper;
import com.full_party.party.service.PartyService;
import com.full_party.tag.service.TagService;
import com.full_party.user.entity.User;
import com.full_party.user.service.UserService;
import com.full_party.util.Utility;
import com.full_party.values.NotificationInfo;
import com.full_party.values.PartyState;
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
    private final NotificationService notificationService;
    private final PartyMapper partyMapper;
    private final CommentMapper commentMapper;
    private static final String PARTY_DEFAULT_URL = "/parties";

    public PartyController(PartyService partyService, TagService tagService, UserService userService, CommentService commentService, NotificationService notificationService, PartyMapper partyMapper, CommentMapper commentMapper) {
        this.partyService = partyService;
        this.tagService = tagService;
        this.userService = userService;
        this.commentService = commentService;
        this.notificationService = notificationService;
        this.partyMapper = partyMapper;
        this.commentMapper = commentMapper;
    }

    // # 기본 CRUD
    // 파티장 : 퀘스트 생성
    @PostMapping
    public ResponseEntity postParty(@RequestBody PartyRequestDto partyRequestDto,
                                    @AuthenticationPrincipal UserDetails userDetails) {

        Party party = partyService.createParty(
                partyMapper.partyRequestDtoToParty(partyRequestDto),
                userService.findUser(userDetails.getUsername())
        );

        tagService.createTagList(party, partyRequestDto.getTags());

        URI uri =
                UriComponentsBuilder
                        .newInstance()
                        .path(PARTY_DEFAULT_URL + "/{party-id}")
                        .buildAndExpand(party.getId())
                        .toUri();

        System.out.println("🟥" + uri);

        return ResponseEntity.created(uri).build();
    }

    // 공통 : 내 파티 및 지역 파티 목록 조회
    @GetMapping
    public ResponseEntity getRelatedPartyList(@RequestParam(name = "region") String region,
                                              @AuthenticationPrincipal UserDetails userDetails) {

        Long userId = userService.findUser(userDetails.getUsername()).getId();
        List<PartyResponseDto> myParties = partyMapper.mapEachPartyToPartyResponseDto(partyService.findProgressingMyParty(userId));
        List<PartyResponseDto> localParties = partyMapper.mapEachPartyToPartyResponseDto(partyService.findLocalParties(userId, region));

        return new ResponseEntity(partyMapper.mapToPartyListResponseDto(myParties, localParties), HttpStatus.OK);
    }

    // 공통 : 파티 정보 조회
    @GetMapping("/{party-id}")
    public ResponseEntity getPartyInfo(@PathVariable("party-id") Long partyId,
                                       @AuthenticationPrincipal UserDetails userDetails) {

        User user = userService.findUser(userDetails.getUsername());
        Party party = partyService.findParty(user.getId(), partyId);
        PartyResponseDto partyResponseDto = partyMapper.partyToPartyResponseDto(party);

        commentService.findComments(partyId).stream()
                .map(comment -> commentMapper.mapToCommentResponseDto(comment))
                .forEach(commentResponseDto -> {
                    List<CommentResponseDto> replies = commentService.findReplies(commentResponseDto.getId()).stream()
                            .map(reply -> commentMapper.mapToCommentResponseDto(reply))
                            .collect(Collectors.toList());
                    partyResponseDto.getComments().add(new CommentReplyDto(commentResponseDto, replies));
                }
        );

        return new ResponseEntity(partyResponseDto, HttpStatus.OK);
    }

    // 파티장 : 파티 삭제
    @DeleteMapping("/{party-id}")
    public ResponseEntity deleteParty(@PathVariable("party-id") Long partyId) {

        Party party = partyService.findParty(partyId);

        // ‼️ 파티원
        List<Party.PartyMember> partyMembers = partyService.findPartyMembers(party, true);

        partyMembers.stream()
                .forEach(partyMember -> notificationService.createNotification(
                        userService.findUser(partyMember.getId()),
                        null,
                        NotificationInfo.DISMISS,
                        null
                )
        );

        partyService.deleteParty(party);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    // 🟥 여기까지 기존 quest controller

    // 참여 신청
    @PostMapping("/{party-id}/application")
    public ResponseEntity applyParty(@PathVariable("party-id") Long partyId,
                                     @AuthenticationPrincipal UserDetails userDetails,
                                     @RequestBody WaiterDto waiterDto) {

        Long userId = Utility.getUserId(userDetails);
        partyService.createWaiter(userId, partyId, waiterDto.getMessage());

        // ‼️ 파티장
        notificationService.createNotification(
                partyService.findParty(partyId).getUser(),
                partyService.findParty(partyId),
                NotificationInfo.APPLY,
                userId
        );

        return new ResponseEntity(HttpStatus.CREATED);
    }

    // 참여 메세지 수정
    @PatchMapping("/{party-id}/users/{user-id}/message")
    public ResponseEntity patchMessage(@PathVariable("party-id") Long partyId,
                                       @PathVariable("user-id") Long userId,
                                       @AuthenticationPrincipal UserDetails userDetails,
                                       @RequestBody PartyMemberDto partyMemberDto) {

        partyService.updateMessage(userId, partyId, partyMemberDto.getMessage());

        return new ResponseEntity(HttpStatus.OK);

//        Waiter waiter = partyService.updateWaiterMessage(partyMapper.waiterDtoToWaiter(partyMemberDto));
//
//        return new ResponseEntity(partyMapper.waiterToWaiterDto(waiter), HttpStatus.OK);
    }

    // 파티 참여 승인 🟥 Header userId -> 파티장 본인 -> RequestBody의 userId가 승인 대상임.
    @PostMapping("/{party-id}/participation/{user-id}")
    public ResponseEntity acceptUser(@PathVariable("party-id") Long partyId,
                                     @PathVariable("user-id") Long userId) {

//        partyService.createUserParty(partyApplyDto.getUserId(), partyApplyDto.getPartyId());

        partyService.createUserParty(userId, partyId);

        // ‼️ 파티원
        notificationService.createNotification(
                userService.findUser(userId),
                partyService.findParty(partyId),
                NotificationInfo.ACCEPT,
                null
        );

        return new ResponseEntity(HttpStatus.CREATED);
    }

    // 참여 신청 취소 및 거절 🟥 Header userId -> 파티장 본인일 수도 있고, 파티원일 수도 있음.
    // 파티장이 거절 -> 알림에서 거절당했다고 표기 필요
    @DeleteMapping("/{party-id}/application/{user-id}")
    public ResponseEntity deleteApplication(@PathVariable("party-id") Long partyId,
                                            @PathVariable("user-id") Long userId,
                                            @AuthenticationPrincipal UserDetails userDetails) {

        // ‼️ 파티장 || 파티원
        if (userId == Utility.getUserId(userDetails)) { // 취소

            Party party = partyService.findParty(partyId);

            notificationService.createNotification(
                    party.getUser(),
                    party,
                    NotificationInfo.CANCEL,
                    userId
            );

        }
        else { // 거절

            notificationService.createNotification(
                    userService.findUser(userId),
                    partyService.findParty(partyId),
                    NotificationInfo.DENY,
                    null
            );
        }

        partyService.deleteWaiter(userId, partyId);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    // 파티 탈퇴 및 강퇴 🟥 Header userId -> 파티장 본인일 수도 있고, 파티원일 수도 있음.
    @DeleteMapping("/{party-id}/participation/{user-id}")
    public ResponseEntity withdrawParty(@PathVariable("party-id") Long partyId,
                                        @PathVariable("user-id") Long userId,
                                        @AuthenticationPrincipal UserDetails userDetails) {

        // userDetails의 userId와 Path의 UserID가 일치하면 탈퇴
        // 일치하지 않으면 강퇴

        // ‼️ 파티장 || 파티원
        if (userId == Utility.getUserId(userDetails)) {

            Party party = partyService.findParty(partyId);

            notificationService.createNotification(
                    party.getUser(),
                    party,
                    NotificationInfo.QUIT,
                    userId
            );

        }
        else {
            notificationService.createNotification(
                    userService.findUser(userId),
                    partyService.findParty(partyId),
                    NotificationInfo.EXPEL,
                    null
            );
        }

        partyService.deleteUserParty(userId, partyId);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    // 파티 정보 수정
    @PatchMapping("/{party-id}")
    public ResponseEntity patchParty(@PathVariable("party-id") Long partyId,
                                     @RequestBody PartyRequestDto partyRequestDto) {

        System.out.println("partyRequestDto.getId() = " + partyRequestDto.getId());
        System.out.println("partyRequestDto.getName() = " + partyRequestDto.getName());
        System.out.println("partyRequestDto.getImage() = " + partyRequestDto.getImage());
        System.out.println("partyRequestDto.getContent() = " + partyRequestDto.getContent());
        System.out.println("partyRequestDto.getStartDate() = " + partyRequestDto.getStartDate());
        System.out.println("partyRequestDto.getEndDate() = " + partyRequestDto.getEndDate());
        System.out.println("partyRequestDto.getIsOnline() = " + partyRequestDto.getIsOnline());
        System.out.println("partyRequestDto.getPrivateLink() = " + partyRequestDto.getPrivateLink());
        System.out.println("partyRequestDto.getRegion() = " + partyRequestDto.getRegion());
        System.out.println("partyRequestDto.getLocation() = " + partyRequestDto.getLocation());
        System.out.println("partyRequestDto.getMemberLimit() = " + partyRequestDto.getMemberLimit());
        System.out.println("partyRequestDto.getCoordinates() = " + partyRequestDto.getCoordinates());
        System.out.println("partyRequestDto.getPartyState() = " + partyRequestDto.getPartyState());
        partyRequestDto.getTags().stream().forEach(tag -> System.out.println(tag));







        partyRequestDto.setId(partyId);

        Party party = partyService.updateParty(partyMapper.partyRequestDtoToParty(partyRequestDto));

        return new ResponseEntity(partyMapper.partyToPartyResponseDto(party), HttpStatus.OK);
    }

    // 파티 상태 변경
    @PatchMapping("/{party-id}/state")
    public ResponseEntity patchPartyState(@PathVariable("party-id") Long partyId,
                                          @RequestParam("party_state") String partyStateStr) {

        PartyState partyState = PartyState.fromString(partyStateStr);

        Party party = partyService.updatePartyState(partyId, partyState);

        NotificationInfo notificationInfo;
        if (partyState == PartyState.FULL_PARTY) notificationInfo = NotificationInfo.FULL_PARTY;
        else if (partyState == PartyState.RECRUITING) notificationInfo = NotificationInfo.RE_PARTY;
        else if (partyState == PartyState.COMPLETED) {
            notificationInfo = NotificationInfo.COMPLETE;
            // 파티원에게만 리뷰 요청 전송
            partyService.findPartyMembers(party, false).stream()
                    .forEach(partyMember -> notificationService.createNotification(
                                    userService.findUser(partyMember.getId()),
                                    party,
                                    NotificationInfo.REVIEW,
                                    null
                            )
                    );

        }
        else throw new BusinessLogicException(ExceptionCode.PARTY_STATE_NOT_FOUND);

        partyService.findPartyMembers(party, true).stream()
                .forEach(partyMember -> notificationService.createNotification(
                        userService.findUser(partyMember.getId()),
                        party,
                        notificationInfo,
                        null
                )
        );

        return new ResponseEntity(partyMapper.partyToPartyResponseDto(party), HttpStatus.OK);

    }

    // 파티원 리뷰
    @PostMapping("/{party-id}/review")
    public ResponseEntity postReview(@PathVariable("party-id") Long partyId,
                                     @AuthenticationPrincipal UserDetails userDetails,
                                     @RequestBody PartyReviewDto partyReviewDto) {

        // 파티장이면 userParty에 isReviewed 체크 안해도 됨. 파티원이면 해야 함.

        partyReviewDto.getResults().stream()
                .forEach(result -> userService.updateExp(result.getUserId(), result.getExp()));

        partyService.checkIsReviewed(
                userService.findUser(userDetails.getUsername()),
                partyService.findParty(partyId),
                partyReviewDto.getResults().size()
        );

        return new ResponseEntity(HttpStatus.OK);
    }

}
