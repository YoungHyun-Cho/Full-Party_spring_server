package com.full_party.party.controller;

import com.full_party.comment.dto.CommentReplyDto;
import com.full_party.comment.dto.CommentResponseDto;
import com.full_party.comment.mapper.CommentMapper;
import com.full_party.comment.service.CommentService;
import com.full_party.exception.BusinessLogicException;
import com.full_party.exception.ExceptionCode;
import com.full_party.notification.service.NotificationService;
import com.full_party.party.dto.*;
import com.full_party.party.entity.Party;
import com.full_party.party.mapper.PartyMapper;
import com.full_party.party.service.PartyService;
import com.full_party.tag.entity.Tag;
import com.full_party.tag.service.TagService;
import com.full_party.user.entity.User;
import com.full_party.user.service.UserService;
import com.full_party.util.Utility;
import com.full_party.values.Level;
import com.full_party.values.NotificationInfo;
import com.full_party.values.PartyState;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/parties")
public class PartyController {

    private final PartyService partyService;
    private final TagService tagService;
    private final UserService userService;
    private final CommentService commentService;
    private final NotificationService notificationService;
    private final PartyMapper partyMapper;
    private final CommentMapper commentMapper;
    private static final String PARTY_DEFAULT_URL = "/parties";

    @PostMapping
    public ResponseEntity postParty(@Valid @RequestBody PartyRequestDto partyRequestDto,
                                    @AuthenticationPrincipal UserDetails userDetails) {

        Party party = partyService.createParty(
                partyMapper.partyRequestDtoToParty(partyRequestDto),
                userService.findUser(userDetails.getUsername())
        );

        List<Tag> tagList = tagService.createTagList(party, partyRequestDto.getTags());
        party.setTagList(tagList);

        URI uri =
                UriComponentsBuilder
                        .newInstance()
                        .path(PARTY_DEFAULT_URL + "/{party-id}")
                        .buildAndExpand(party.getId())
                        .toUri();

        return ResponseEntity.created(uri).build();
    }

    @GetMapping
    public ResponseEntity getRelatedPartyList(@RequestParam(name = "region") String region,
                                              @AuthenticationPrincipal UserDetails userDetails) {

        User user = userService.findUser(userDetails.getUsername());
        List<PartyResponseDto> myParties = partyMapper.mapEachPartyToPartyResponseDto(partyService.findProgressingMyParty(user.getId()));
        List<PartyResponseDto> localParties = partyMapper.mapEachPartyToPartyResponseDto(partyService.findLocalParties(user.getId(), region));

        return new ResponseEntity(
                partyMapper.mapToPartyListResponseDto(
                        myParties, localParties, user.getCoordinates(),
                        notificationService.checkNotificationBadge(user.getId())
                ),
                HttpStatus.OK
        );
    }

    @GetMapping("/{party-id}")
    public ResponseEntity getPartyInfo(@PathVariable("party-id") Long partyId,
                                       @AuthenticationPrincipal UserDetails userDetails) {

        User user = userService.findUser(userDetails.getUsername());
        Party party = partyService.findParty(user.getId(), partyId);

        if (party.getPartyState() == PartyState.DISMISSED) throw new BusinessLogicException(ExceptionCode.PARTY_NOT_FOUND);

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

        partyResponseDto.setNotificationBadge(notificationService.checkNotificationBadge(user.getId()));

        return new ResponseEntity(partyResponseDto, HttpStatus.OK);
    }

    @DeleteMapping("/{party-id}")
    public ResponseEntity deleteParty(@PathVariable("party-id") Long partyId) {

        Party party = partyService.findParty(partyId);

        List<Party.PartyMember> partyMembers = partyService.findPartyMembers(party, true);

        partyMembers.stream()
                .forEach(partyMember -> notificationService.createNotification(
                        userService.findUser(partyMember.getId()),
                        party,
                        NotificationInfo.DISMISS,
                        null
                )
        );

        partyService.deleteParty(party);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{party-id}/application")
    public ResponseEntity applyParty(@PathVariable("party-id") Long partyId,
                                     @AuthenticationPrincipal UserDetails userDetails,
                                     @RequestBody WaiterDto waiterDto) {

        Long userId = Utility.getUserId(userDetails);
        partyService.createWaiter(userId, partyId, waiterDto.getMessage());

        notificationService.createNotification(
                partyService.findParty(partyId).getUser(),
                partyService.findParty(partyId),
                NotificationInfo.APPLY,
                userService.findUser(userId)
        );

        return new ResponseEntity(HttpStatus.CREATED);
    }

    @PatchMapping("/{party-id}/users/{user-id}/message")
    public ResponseEntity patchMessage(@PathVariable("party-id") Long partyId,
                                       @PathVariable("user-id") Long userId,
                                       @AuthenticationPrincipal UserDetails userDetails,
                                       @RequestBody PartyMemberDto partyMemberDto) {

        partyService.updateMessage(userId, partyId, partyMemberDto.getMessage());

        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/{party-id}/participation/{user-id}")
    public ResponseEntity acceptUser(@PathVariable("party-id") Long partyId,
                                     @PathVariable("user-id") Long userId) {

        partyService.createUserParty(userId, partyId);

        notificationService.createNotification(
                userService.findUser(userId),
                partyService.findParty(partyId),
                NotificationInfo.ACCEPT,
                null
        );

        return new ResponseEntity(HttpStatus.CREATED);
    }

    @DeleteMapping("/{party-id}/application/{user-id}")
    public ResponseEntity deleteApplication(@PathVariable("party-id") Long partyId,
                                            @PathVariable("user-id") Long userId,
                                            @AuthenticationPrincipal UserDetails userDetails) {

        if (userId == Utility.getUserId(userDetails)) {

            Party party = partyService.findParty(partyId);

            notificationService.createNotification(
                    party.getUser(),
                    party,
                    NotificationInfo.CANCEL,
                    userService.findUser(userId)
            );

        }
        else {

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

    @DeleteMapping("/{party-id}/participation/{user-id}")
    public ResponseEntity withdrawParty(@PathVariable("party-id") Long partyId,
                                        @PathVariable("user-id") Long userId,
                                        @AuthenticationPrincipal UserDetails userDetails) {

        if (userId == Utility.getUserId(userDetails)) {

            Party party = partyService.findParty(partyId);

            notificationService.createNotification(
                    party.getUser(),
                    party,
                    NotificationInfo.QUIT,
                    userService.findUser(userId)
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

    @PatchMapping("/{party-id}")
    public ResponseEntity patchParty(@PathVariable("party-id") Long partyId,
                                     @Valid @RequestBody PartyRequestDto partyRequestDto) {

        partyRequestDto.setId(partyId);

        Party updatedParty = partyService.updateParty(
                partyMapper.partyRequestDtoToParty(partyRequestDto),
                partyRequestDto.getTags()
        );

        return new ResponseEntity(partyMapper.partyToPartyResponseDto(updatedParty), HttpStatus.OK);
    }

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

    @PostMapping("/{party-id}/review")
    public ResponseEntity postReview(@PathVariable("party-id") Long partyId,
                                     @AuthenticationPrincipal UserDetails userDetails,
                                     @RequestBody PartyReviewDto partyReviewDto) {

        partyReviewDto.getResults().stream()
                .forEach(reviewResult -> {
                    Level.Result calculationResult = userService.updateExp(reviewResult.getUserId(), reviewResult.getExp());
                    if (calculationResult.getNotificationInfo() != null) {
                        notificationService.createNotification(
                                userService.findUser(reviewResult.getUserId()),
                                partyService.findParty(partyId),
                                calculationResult.getNotificationInfo(),
                                null
                        );
                    }
                }
        );

        partyService.changeIsReviewed(
                userService.findUser(userDetails.getUsername()),
                partyService.findParty(partyId),
                partyReviewDto.getResults().size()
        );

        return new ResponseEntity(HttpStatus.OK);
    }

}
