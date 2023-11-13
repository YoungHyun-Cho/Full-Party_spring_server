package com.full_party.comment.entity;

import com.full_party.user.entity.User;

import java.time.LocalDateTime;

public interface QnA {
    Long getId();
    User getUser();
    String getContent();
    LocalDateTime getCreatedAt();
}
