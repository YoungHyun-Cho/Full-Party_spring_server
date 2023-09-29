package com.full_party.heart.dto;

import com.full_party.heart.entity.Heart;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class HeartResponseDto {

    private List<Heart> heartList;
}
