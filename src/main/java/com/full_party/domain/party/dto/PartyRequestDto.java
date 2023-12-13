package com.full_party.domain.party.dto;

import com.full_party.global.values.Coordinates;
import com.full_party.global.values.PartyState;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PartyRequestDto {

    private Long id;
    private Long userId;
    private String image;
    private String region;
    private Integer memberLimit;
    private Date startDate;
    private Date endDate;
    private Coordinates coordinates;
    private Boolean isOnline;
    private ArrayList<String> tags;

    @NotBlank
    private String name;

    @NotBlank
    private String content;

    @NotBlank
    private String location;

    @NotBlank
    private String privateLink;

    private PartyState partyState;
}
