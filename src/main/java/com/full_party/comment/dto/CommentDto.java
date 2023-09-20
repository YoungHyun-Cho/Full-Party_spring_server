package com.full_party.comment.dto;

import lombok.Getter;

@Getter
public class CommentDto {

    private Long userId;
    private Long partyId;
    private String content;
}
