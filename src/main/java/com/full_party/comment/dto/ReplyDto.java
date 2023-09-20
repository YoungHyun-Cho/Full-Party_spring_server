package com.full_party.comment.dto;

import lombok.Getter;

@Getter
public class ReplyDto {

    private Long userId;
    private Long commentId;
    private String content;
}
