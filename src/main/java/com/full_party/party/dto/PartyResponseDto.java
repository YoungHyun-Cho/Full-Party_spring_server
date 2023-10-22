package com.full_party.party.dto;

import com.full_party.user.dto.UserBasicResponseDto;
import com.full_party.values.Coordinates;
import com.full_party.values.PartyState;
import lombok.Getter;
import lombok.Setter;

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
    private List<UserBasicResponseDto> members; // 제공에 필요한 정보만 mapper에서 추출
}
