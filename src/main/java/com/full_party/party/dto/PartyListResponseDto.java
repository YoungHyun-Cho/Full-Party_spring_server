package com.full_party.party.dto;

import com.full_party.quest.dto.QuestResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@AllArgsConstructor
public class PartyListResponseDto {

        private List<PartyResponseDto> myParties;
        private List<PartyResponseDto> localParties;
}
