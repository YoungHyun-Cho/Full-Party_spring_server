package com.full_party.comment.controller;

import com.full_party.comment.dto.CommentDto;
import com.full_party.comment.dto.ReplyDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/comments")
public class CommentController {

    // 댓글 등록
    @PostMapping
    public ResponseEntity postComment(@RequestBody CommentDto commentDto) {
        return new ResponseEntity(commentDto, HttpStatus.CREATED);
    }

    // 대댓글 등록
    @PostMapping("{comment-id}/reply")
    public ResponseEntity postReply(@RequestBody ReplyDto replyDto,
                                    @PathVariable("comment-id") Long commentId) {
        return new ResponseEntity(replyDto, HttpStatus.CREATED);
    }

    // 댓글 수정
    @PatchMapping("/{comment-id}")
    public ResponseEntity patchComment(@RequestBody CommentDto commentDto,
                                       @PathVariable("comment-id") Long commentId) {
        return new ResponseEntity(commentDto, HttpStatus.OK);
    }

    // 대댓글 수정
    @PatchMapping("/{comment-id}/reply/{reply-id}")
    public ResponseEntity patchReply(@RequestBody ReplyDto replyDto,
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
    @DeleteMapping("/{reply-id}")
    public ResponseEntity deleteReply(@PathVariable("reply-id") Long replyId) {
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
