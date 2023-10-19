package com.full_party.quest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class QuestListResponseDto {

    private List<QuestResponseDto> myQuests;
    private List<QuestResponseDto> localQuests;
}
