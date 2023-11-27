package com.full_party.comment.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class CommentPostDto {

    private Long userId;
    private Long partyId;

    @NotBlank
    private String content;
}
