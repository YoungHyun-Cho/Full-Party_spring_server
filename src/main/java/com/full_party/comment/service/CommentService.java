package com.full_party.comment.service;

import com.full_party.comment.entity.Comment;
import com.full_party.comment.repository.CommentRepository;
import com.full_party.exception.BusinessLogicException;
import com.full_party.exception.ExceptionCode;
import com.full_party.party.service.PartyService;
import com.full_party.user.service.UserService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserService userService;
    private final PartyService partyService;

    public CommentService(CommentRepository commentRepository, UserService userService, PartyService partyService) {
        this.commentRepository = commentRepository;
        this.userService = userService;
        this.partyService = partyService;
    }

    public Comment createComment(Comment comment) {

        comment.setUser(userService.findUser(comment.getUser().getId()));
        comment.setParty(partyService.findParty(comment.getParty().getId()));

        return commentRepository.save(comment);
    }

    private Comment findComment(Long commentId) {

        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        Comment comment = optionalComment.orElseThrow(() -> new BusinessLogicException(ExceptionCode.COMMENT_NOT_FOUND));
        return comment;
    }

    public Comment updateComment(Comment comment) {

        Comment foundComment = findComment(comment.getId());

        foundComment.setContent(comment.getContent());

        return commentRepository.save(foundComment);
    }

    public void deleteComment(Long commentId) {
        Comment foundComment = findComment(commentId);
        commentRepository.delete(foundComment);
    }
}
