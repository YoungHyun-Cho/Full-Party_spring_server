package com.full_party.quest.mapper;

import com.full_party.quest.dto.QuestDto;
import com.full_party.quest.dto.QuestListResponseDto;
import com.full_party.quest.dto.QuestResponseDto;
import com.full_party.quest.entity.Quest;
import com.full_party.tag.entity.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Mapper(componentModel = "spring")
public interface QuestMapper {
    Quest questDtoToQuest(QuestDto questDto);

    @Mapping(source = "tagList", target = "tags", qualifiedByName = "listToString")
    QuestResponseDto questToQuestResponseDto(Quest quest);

    @Named("listToString")
    default ArrayList<String> listToString(List<Tag> tagList) {

        ArrayList<String> tags = new ArrayList<>();

        tagList.stream()
                .forEach(tag -> tags.add(tag.getValue()));

        return tags;
    }

    default QuestListResponseDto mapToQuestListResponseDto(List<QuestResponseDto> myQuests, List<QuestResponseDto> localQuests) {

        return new QuestListResponseDto(myQuests, localQuests);
    }
}
