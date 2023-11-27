package com.full_party.comment.service;

import com.full_party.comment.entity.Comment;
import com.full_party.comment.entity.Reply;
import com.full_party.comment.repository.CommentRepository;
import com.full_party.comment.repository.ReplyRepository;
import com.full_party.exception.BusinessLogicException;
import com.full_party.exception.ExceptionCode;
import com.full_party.party.service.PartyService;
import com.full_party.user.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final ReplyRepository replyRepository;

    public CommentService(CommentRepository commentRepository, ReplyRepository replyRepository) {
        this.commentRepository = commentRepository;
        this.replyRepository = replyRepository;
    }

    public Comment createComment(Comment comment) {
        return commentRepository.save(comment);
    }

    public Comment findComment(Long commentId) {
        return commentRepository.findById(commentId).get();
    }

    public List<Comment> findComments(Long partyId) {
        return commentRepository.findByPartyId(partyId);
    }

    public List<Reply> findReplies(Long commentId) {
        return replyRepository.findByCommentId(commentId);
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

    public Reply createReply(Reply reply) {
        return replyRepository.save(reply);
    }


}
