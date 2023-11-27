package com.full_party.comment.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class ReplyPostDto {

    private Long userId;
    private Long commentId;

    @NotBlank
    private String content;
}
