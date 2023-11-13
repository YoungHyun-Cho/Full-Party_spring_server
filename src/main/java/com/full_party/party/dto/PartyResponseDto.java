package com.full_party.party.dto;

import com.full_party.comment.dto.CommentReplyDto;
import com.full_party.user.dto.UserBasicResponseDto;
import com.full_party.values.Coordinates;
import com.full_party.values.PartyState;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class PartyResponseDto {
    private Long id;
    private Long userId;
    private String name;
    private String image;
    private String region;
    private String location;
    private String privateLink;
    private String content;
    private Integer memberLimit;
    private Integer heartCount;
    private Boolean isOnline;
    private Boolean isHeart;
    private Date startDate;
    private Date endDate;
    private Coordinates coordinates;
    private PartyState partyState;
    private List<CommentReplyDto> comments = new ArrayList<>();
    private List<String> tags;
    private List<PartyMemberDto> memberList;
    private List<PartyMemberDto> waiterList;
}
