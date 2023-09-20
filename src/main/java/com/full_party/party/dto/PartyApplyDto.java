package com.full_party.party.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
public class PartyApplyDto {

    @Setter
    private Long partyId;
    private Long userId;
    private String message;
}
