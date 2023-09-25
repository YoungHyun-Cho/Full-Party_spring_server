package com.full_party.quest.dto;

import com.full_party.values.Coordinates;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;

@Getter
@Setter
public class QuestDto {
    // Mapper에서 Quest와 Party로 갈라줘야 함.
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

/*
* # 문제
* - questDto의 tags와 quest(엔티티)의 tags 간의 타입이 불일치
*   - questDto.tags : ArrayList<String> -> 태그 문자열을 담고 있음.
*   - quest.tags : ArrayList<Tags> -> DB 상의 tag 테이블과의 관계 매핑
*
* # 필요 동작
* - questDto.tags의 내용이 DB상의 tags에 반영되어야 함.
* */