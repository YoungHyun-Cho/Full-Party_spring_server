package com.full_party.values;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Coordinates {

    private Double latitude;  // 위도
    private Double longitude; // 경도
}
