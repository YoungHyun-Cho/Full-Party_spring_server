package com.full_party.party.dto;

import com.full_party.values.Coordinates;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Date;

@Getter
public class PartyInfo {

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
