package com.full_party.comment.controller;

import com.full_party.comment.dto.CommentPostDto;
import com.full_party.comment.dto.ReplyPostDto;
import com.full_party.comment.entity.Comment;
import com.full_party.comment.entity.Reply;
import com.full_party.comment.mapper.CommentMapper;
import com.full_party.comment.service.CommentService;
import com.full_party.notification.service.NotificationService;
import com.full_party.party.entity.Party;
import com.full_party.party.service.PartyService;
import com.full_party.user.service.UserService;
import com.full_party.util.Utility;
import com.full_party.values.NotificationInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;
    private final UserService userService;
    private final CommentMapper commentMapper;
    private final NotificationService notificationService;
    private final PartyService partyService;

    // 댓글 등록
    @PostMapping
    public ResponseEntity postComment(@Valid @RequestBody CommentPostDto commentDto,
                                      @AuthenticationPrincipal UserDetails userDetails) {

        Long userId = Utility.getUserId(userDetails);
        commentDto.setUserId(userId);

        Comment comment = commentService.createComment(commentMapper.commentDtoToComment(commentDto));

        Party party = partyService.findParty(commentDto.getPartyId());
        notificationService.createNotification(
                party.getUser(),
                party,
                NotificationInfo.COMMENT,
                userService.findUser(userId)
        );

        return new ResponseEntity(commentMapper.mapToCommentResponseDto(comment), HttpStatus.CREATED);
    }

    // 대댓글 등록
    @PostMapping("{comment-id}/replies")
    public ResponseEntity postReply(@Valid @RequestBody ReplyPostDto replyPostDto,
                                    @PathVariable("comment-id") Long commentId,
                                    @AuthenticationPrincipal UserDetails userDetails) {

        Long userId = Utility.getUserId(userDetails);
        replyPostDto.setUserId(userId);
        replyPostDto.setCommentId(commentId);

        Reply reply = commentService.createReply(commentMapper.replyPostDtoToReply(replyPostDto));

        Comment comment = commentService.findComment(reply.getComment().getId());
        notificationService.createNotification(
                comment.getUser(),
                comment.getParty(),
                NotificationInfo.REPLY,
                userService.findUser(userId)
        );

        return new ResponseEntity(commentMapper.mapToCommentResponseDto(reply), HttpStatus.CREATED);
    }

    // 댓글 삭제
    @DeleteMapping("/{comment-id}")
    public ResponseEntity deleteComment(@PathVariable("comment-id") Long commentId) {

        commentService.deleteComment(commentId);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    // 대댓글 삭제
    @DeleteMapping("/{comment-id}/replies/{reply-id}")
    public ResponseEntity deleteReply(@PathVariable("reply-id") Long replyId) {

        commentService.deleteReply(replyId);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
