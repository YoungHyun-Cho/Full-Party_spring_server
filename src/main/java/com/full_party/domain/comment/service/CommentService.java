package com.full_party.domain.comment.service;

import com.full_party.domain.comment.entity.Comment;
import com.full_party.domain.comment.entity.Reply;
import com.full_party.domain.comment.repository.CommentRepository;
import com.full_party.domain.comment.repository.ReplyRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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


    public Reply findReply(Long replyId) {
        return replyRepository.findById(replyId).get();
    }

    public void deleteComment(Long commentId) {
        Comment foundComment = findComment(commentId);
        commentRepository.delete(foundComment);
    }

    public void deleteReply(Long replyId) {
        Reply foundReply = findReply(replyId);
        replyRepository.delete(foundReply);
    }

    public Reply createReply(Reply reply) {
        return replyRepository.save(reply);
    }


}
