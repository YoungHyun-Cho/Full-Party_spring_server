package com.full_party.domain.comment.repository;

import com.full_party.domain.comment.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Long> {

    List<Reply> findByCommentId(Long commentId);
}
