package com.full_party.global.values;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Coordinates {

    private Double lat;  // 위도
    private Double lng; // 경도
}
