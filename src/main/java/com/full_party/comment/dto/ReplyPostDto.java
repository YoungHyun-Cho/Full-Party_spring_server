package com.full_party.comment.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReplyPostDto {

    private Long userId;
    private Long commentId;
    private String content;
}
