package com.full_party.heart.dto;

import com.full_party.heart.entity.Heart;
import com.full_party.party.dto.PartyResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HeartResponseDto {

    private List<PartyResponseDto> parties;

    // 원인 : DTO가 아닌 엔티티를 리턴해서 문제가 발생함.
    // 해결
    // - 좋아요 누른 파티 정보를 추출해서 리스트 DTO로 리턴해야 함.
    // 결국 파티에 대한 정보이니, PartyService에서 정보 제공해야 할 듯
}
