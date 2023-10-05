package com.full_party.comment.mapper;

import com.full_party.comment.dto.CommentDto;
import com.full_party.comment.entity.Comment;
import com.full_party.party.entity.Party;
import com.full_party.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(source = "userId", target = "user", qualifiedByName = "userIdToUser")
    @Mapping(source = "partyId", target = "party", qualifiedByName = "partyIdToParty")
    Comment commentDtoToComment(CommentDto commentDto);

    @Mapping(source = "user", target = "userId", qualifiedByName = "userToUserId")
    @Mapping(source = "party", target = "partyId", qualifiedByName = "partyToPartyId")
    CommentDto commentToCommentDto(Comment comment);

    @Named("userIdToUser")
    default User userIdToUser(Long userId) {
        return new User(userId);
    }

    @Named("partyIdToParty")
    default Party partyIdToParty(Long partyId) {
        return new Party(partyId);
    }

    @Named("userToUserId")
    default Long userToUserId(User user) {
        return user.getId();
    }

    @Named("partyToPartyId")
    default Long partyToPartyId(Party party) {
        return party.getId();
    }
}
