package com.full_party.party.dto;

import com.full_party.comment.dto.CommentReplyDto;
import com.full_party.user.dto.UserBasicResponseDto;
import com.full_party.values.Coordinates;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class PartyResponseDto {
    private Long id;
    private String name;
    private String image;
    private Integer memberLimit;
    private String content;
    private Date startDate;
    private Date endDate;
    private Boolean isOnline;
    private String region;
    private String location;
    private Coordinates coordinates;
    private Boolean isHeart;
    private List<CommentReplyDto> comments = new ArrayList<>();
    private List<String> tags;
    private List<UserBasicResponseDto> memberList;
    private List<UserBasicResponseDto> waiterList;
}
