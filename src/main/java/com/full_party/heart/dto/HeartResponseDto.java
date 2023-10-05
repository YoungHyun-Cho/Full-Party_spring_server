package com.full_party.heart.dto;

import com.full_party.heart.entity.Heart;
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

    private List<Heart> heartList;
}
