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
import com.full_party.util.Utility;
import com.full_party.values.NotificationInfo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;
    private final CommentMapper commentMapper;
    private final NotificationService notificationService;
    private final PartyService partyService;

    public CommentController(CommentService commentService, CommentMapper commentMapper, NotificationService notificationService, PartyService partyService) {
        this.commentService = commentService;
        this.commentMapper = commentMapper;
        this.notificationService = notificationService;
        this.partyService = partyService;
    }

    // 댓글 등록
    @PostMapping
    public ResponseEntity postComment(@RequestBody CommentPostDto commentDto,
                                      @AuthenticationPrincipal UserDetails userDetails) {

        Long userId = Utility.getUserId(userDetails);
        commentDto.setUserId(userId);

        Comment comment = commentService.createComment(commentMapper.commentDtoToComment(commentDto));

        Party party = partyService.findParty(commentDto.getPartyId());
        notificationService.createNotification(
                party.getUser(),
                party,
                NotificationInfo.COMMENT,
                userId
        );

        return new ResponseEntity(commentMapper.mapToCommentResponseDto(comment), HttpStatus.CREATED);
    }

    // 대댓글 등록
    @PostMapping("{comment-id}/reply")
    public ResponseEntity postReply(@RequestBody ReplyPostDto replyPostDto,
                                    @PathVariable("comment-id") Long commentId,
                                    @AuthenticationPrincipal UserDetails userDetails) {

        Long userId = Utility.getUserId(userDetails);
        replyPostDto.setUserId(userId);

        Reply reply = commentService.createReply(commentMapper.replyPostDtoToReply(replyPostDto));

        Comment comment = commentService.findComment(reply.getComment().getId());
        notificationService.createNotification(
                comment.getUser(),
                comment.getParty(),
                NotificationInfo.REPLY,
                userId
        );

        return new ResponseEntity(commentMapper.mapToCommentResponseDto(reply), HttpStatus.CREATED);
    }

    // 댓글 수정
    @PatchMapping("/{comment-id}")
    public ResponseEntity patchComment(@RequestBody CommentPostDto commentDto,
                                       @PathVariable("comment-id") Long commentId,
                                       @RequestHeader("user-id") Long userId) {

        commentDto.setUserId(userId);

        Comment comment = commentService.updateComment(commentMapper.commentDtoToComment(commentDto));

        return new ResponseEntity(commentMapper.mapToCommentResponseDto(comment), HttpStatus.OK);
    }

    // 대댓글 수정
    @PatchMapping("/{comment-id}/reply/{reply-id}")
    public ResponseEntity patchReply(@RequestBody ReplyPostDto replyDto,
                                     @PathVariable("comment-id") Long commentId,
                                     @PathVariable("reply-id") Long replyId) {
        return new ResponseEntity(replyDto, HttpStatus.OK);
    }

    // 댓글 삭제
    @DeleteMapping("/{comment-id}")
    public ResponseEntity deleteComment(@PathVariable("comment-id") Long commentId) {
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    // 대댓글 삭제
    @DeleteMapping("/{comment-id}/reply/{reply-id}")
    public ResponseEntity deleteReply(@PathVariable("reply-id") Long replyId) {
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
