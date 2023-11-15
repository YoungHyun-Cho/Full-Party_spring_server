package com.full_party.party.dto;

import com.full_party.dto.CommonInformationDto;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PartyListResponseDto extends CommonInformationDto {

        private List<PartyResponseDto> myParties;
        private List<PartyResponseDto> localParties;

        public PartyListResponseDto(Boolean notificationBadge, List<PartyResponseDto> myParties, List<PartyResponseDto> localParties) {
                super(notificationBadge);
                this.myParties = myParties;
                this.localParties = localParties;
        }
}
