package com.full_party.party.dto;

import com.full_party.values.PartyState;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PartyPatchDto {
    private Long id;
    private PartyState partyState;
    private Integer memberLimit;
}
