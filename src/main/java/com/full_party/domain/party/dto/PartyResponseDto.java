package com.full_party.domain.party.dto;

import com.full_party.domain.comment.dto.CommentReplyDto;
import com.full_party.global.dto.CommonInformationDto;
import com.full_party.global.values.Coordinates;
import com.full_party.global.values.PartyState;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PartyResponseDto extends CommonInformationDto {
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
    private Boolean isReviewed;
    private Date startDate;
    private Date endDate;
    private Coordinates coordinates;
    private PartyState partyState;
    private List<CommentReplyDto> comments = new ArrayList<>();
    private List<String> tags;
    private List<PartyMemberDto> memberList;
    private List<PartyMemberDto> waiterList;
}
