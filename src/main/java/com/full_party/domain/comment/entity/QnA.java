package com.full_party.domain.comment.entity;

import com.full_party.domain.user.entity.User;

import java.time.LocalDateTime;

public interface QnA {
    Long getId();
    User getUser();
    String getContent();
    LocalDateTime getCreatedAt();
}
