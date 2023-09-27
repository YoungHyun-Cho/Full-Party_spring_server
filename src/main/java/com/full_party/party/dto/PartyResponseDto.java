package com.full_party.party.dto;

import com.full_party.values.PartyState;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PartyResponseDto {
    private Long id;
    private String partyState;
    private Integer memberLimit;
}
