package com.full_party.domain.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentReplyDto {
    private CommentResponseDto comment;
    private List<CommentResponseDto> replies;
}
