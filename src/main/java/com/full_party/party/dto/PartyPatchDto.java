package com.full_party.party.dto;

import com.full_party.values.Coordinates;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;

@Getter
@Setter
public class PartyPatchDto {

    private Long partyId;
    private PartyInfo partyInfo;
}
