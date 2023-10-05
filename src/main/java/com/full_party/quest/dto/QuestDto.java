package com.full_party.quest.dto;

import com.full_party.values.Coordinates;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;

@Getter
@Setter
public class QuestDto {
    private Long questId;
    private String name;
    private String image;
    private Integer memberLimit;
    private String content;
    private String region;
    private String location;
    private Coordinates coordinates;
    private Date startDate;
    private Date endDate;
    private Boolean isOnline;
    private String privateLink;
    private ArrayList<String> tags;
}

// userId는 추후 토큰으로 전달 받도록