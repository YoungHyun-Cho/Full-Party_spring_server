package com.full_party.party.mapper;

import com.full_party.party.dto.*;
import com.full_party.party.entity.Party;
import com.full_party.party.entity.Waiter;
import com.full_party.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface PartyMapper {

    Party partyPostDtoToParty(PartyPostDto partyPostDto);

    Party partyPatchDtoToParty(PartyPatchDto partyPatchDto);

    PartyResponseDto partyToPartyResponseDto(Party party);
//    @Mapping(source = "userId", target = "user", qualifiedByName = "userIdToTempUser")
//    @Mapping(source = "partyId", target = "party", qualifiedByName = "partyIdToTempParty")
//    Waiter waiterDtoToWaiter(WaiterDto waiterDto);
//
//    @Mapping(source = "user", target = "userId", qualifiedByName = "userToUserId")
//    @Mapping(source = "party", target = "partyId", qualifiedByName = "partyToPartyId")
    WaiterDto waiterToWaiterDto(Waiter waiter);
//
//    @Named("userIdToTempUser")
//    default User userIdToTempUser(Long userId) {
//        return new User(userId);
//    }
//
//    @Named("partyIdToTempParty")
//    default Party partyIdToTempParty(Long partyId) {
//        return new Party(partyId);
//    }
//
//    @Named("userToUserId")
//    default Long userToUserId(User user) {
//        return user.getId();
//    }
//
//    @Named("partyToPartyId")
//    default Long partyToPartyId(Party party) {
//        return party.getId();
//    }

    default PartyListResponseDto mapToPartyListResponseDto(List<PartyResponseDto> myParties, List<PartyResponseDto> localParties) {

        return new PartyListResponseDto(myParties, localParties);
    }

    default List<PartyResponseDto> mapEachPartyToPartyResponseDto(List<Party> partyList) {

        return partyList.stream()
                .map(party -> partyToPartyResponseDto(party))
                .collect(Collectors.toList());
    }
}
