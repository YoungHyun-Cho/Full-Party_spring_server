package com.full_party.values;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Coordinates {

    private Double latitude;  // 위도
    private Double longitude; // 경도
}
