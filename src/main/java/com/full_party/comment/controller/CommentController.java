package com.full_party.comment.controller;

import com.full_party.comment.dto.CommentPostDto;
import com.full_party.comment.dto.ReplyPostDto;
import com.full_party.comment.entity.Comment;
import com.full_party.comment.mapper.CommentMapper;
import com.full_party.comment.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/comments")
public class CommentController {

    private final CommentService commentService;
    private final CommentMapper commentMapper;

    public CommentController(CommentService commentService, CommentMapper commentMapper) {
        this.commentService = commentService;
        this.commentMapper = commentMapper;
    }

    // 댓글 등록
    @PostMapping
    public ResponseEntity postComment(@RequestBody CommentPostDto commentDto,
                                      @RequestHeader("user-id") Long userId) {

        commentDto.setUserId(userId);

        Comment comment = commentService.createComment(commentMapper.commentDtoToComment(commentDto));

        return new ResponseEntity(commentMapper.commentToCommentDto(comment), HttpStatus.CREATED);
    }

    // 대댓글 등록
    @PostMapping("{comment-id}/reply")
    public ResponseEntity postReply(@RequestBody ReplyPostDto replyDto,
                                    @PathVariable("comment-id") Long commentId) {
        return new ResponseEntity(replyDto, HttpStatus.CREATED);
    }

    // 댓글 수정
    @PatchMapping("/{comment-id}")
    public ResponseEntity patchComment(@RequestBody CommentPostDto commentDto,
                                       @PathVariable("comment-id") Long commentId,
                                       @RequestHeader("user-id") Long userId) {

        commentDto.setUserId(userId);

        Comment comment = commentService.updateComment(commentMapper.commentDtoToComment(commentDto));

        return new ResponseEntity(commentMapper.commentToCommentDto(comment), HttpStatus.OK);
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
