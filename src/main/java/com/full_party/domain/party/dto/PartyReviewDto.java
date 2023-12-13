package com.full_party.domain.party.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PartyReviewDto {

    private List<Result> results;

    @Getter
    public static class Result {

        private Long userId;
        private Integer exp;
    }
}
