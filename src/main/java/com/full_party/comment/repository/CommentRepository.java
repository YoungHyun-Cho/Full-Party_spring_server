package com.full_party.comment.repository;

import com.full_party.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Optional<Comment> findByUserIdAndPartyId(Long userId, Long partyId);
}
