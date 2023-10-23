package com.full_party.comment.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentPostDto {

    private Long userId;
    private Long partyId;
    private String content;
}
