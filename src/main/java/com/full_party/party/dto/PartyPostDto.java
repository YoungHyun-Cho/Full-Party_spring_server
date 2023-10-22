package com.full_party.party.dto;

import com.full_party.values.Coordinates;
import com.full_party.values.PartyState;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PartyPostDto {
    private Long userId;
    private String name;
    private String image;
    private String content;
    private Date startDate;
    private Date endDate;
    private Boolean isOnline;
    private String privateLink;
    private String region;
    private String location;
    private Integer memberLimit;
    private Coordinates coordinates;
    private ArrayList<String> tags;
}
