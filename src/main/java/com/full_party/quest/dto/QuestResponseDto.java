package com.full_party.quest.dto;

import com.full_party.values.Coordinates;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;

@Getter
@Setter
public class QuestResponseDto {
    private String name;
    private String image;
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
