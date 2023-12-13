package com.full_party.domain.comment.repository;

import com.full_party.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByPartyId(Long partyId);
}
