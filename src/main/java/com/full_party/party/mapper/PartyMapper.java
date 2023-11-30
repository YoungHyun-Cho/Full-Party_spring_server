package com.full_party.party.mapper;

import com.full_party.party.dto.*;
import com.full_party.party.entity.Party;
import com.full_party.party.entity.Waiter;
import com.full_party.tag.entity.Tag;
import com.full_party.user.entity.User;
import com.full_party.values.Coordinates;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface PartyMapper {

    @Mapping(source = "userId", target = "user", qualifiedByName = "userIdToUser")
    Party partyRequestDtoToParty(PartyRequestDto partyRequestDto);

    @Mapping(source = "tagList", target = "tags", qualifiedByName = "TagListToStringList")
    @Mapping(source = "comments", target = "comments", ignore = true)
    @Mapping(source = "user", target = "userId", qualifiedByName = "userToUserId")
    PartyResponseDto partyToPartyResponseDto(Party party);

    @Named("TagListToStringList")
    default List<String> TagListToStringList(List<Tag> tagList) {

        return tagList.stream()
                .map(tag -> tag.getValue())
                .collect(Collectors.toList());
    }

    default PartyListResponseDto mapToPartyListResponseDto(List<PartyResponseDto> myParties, List<PartyResponseDto> localParties, Coordinates coordinates, Boolean notificationBadge) {

        return new PartyListResponseDto(notificationBadge, myParties, localParties, coordinates);
    }

    default List<PartyResponseDto> mapEachPartyToPartyResponseDto(List<Party> partyList) {

        return partyList.stream()
                .map(party -> partyToPartyResponseDto(party))
                .collect(Collectors.toList());
    }

    default Map<String, List<PartyResponseDto>> mapRelatedPartyMap(List<Party> leadingParties, List<Party> participatingParties, List<Party> completedParties) {

        Map<String, List<PartyResponseDto>> relatedParties = new HashMap<>();

        relatedParties.put("leadingParties", mapEachPartyToPartyResponseDto(leadingParties));
        relatedParties.put("participatingParties", mapEachPartyToPartyResponseDto(participatingParties));
        relatedParties.put("completedParties", mapEachPartyToPartyResponseDto(completedParties));

        return relatedParties;
    }

    @Named("userToUserId")
    default Long userToUserId(User user) {
        return user.getId();
    }

    @Named("userIdToUser")
    default User userIdToUser(Long userId) {
        return new User(userId);
    }

}
