package com.full_party.party.dto;

import com.full_party.dto.CommonInformationDto;
import com.full_party.values.Coordinates;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PartyListResponseDto extends CommonInformationDto {

        private List<PartyResponseDto> myParties;
        private List<PartyResponseDto> localParties;
        private Coordinates coordinates;

        public PartyListResponseDto(Boolean notificationBadge, List<PartyResponseDto> myParties, List<PartyResponseDto> localParties, Coordinates coordinates) {
                super(notificationBadge);
                this.myParties = myParties;
                this.localParties = localParties;
                this.coordinates = coordinates;
        }
}
