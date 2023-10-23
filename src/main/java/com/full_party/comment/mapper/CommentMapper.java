package com.full_party.comment.mapper;

import com.full_party.comment.dto.CommentPostDto;
import com.full_party.comment.dto.CommentResponseDto;
import com.full_party.comment.entity.Comment;
import com.full_party.comment.entity.Reply;
import com.full_party.party.entity.Party;
import com.full_party.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(source = "userId", target = "user", qualifiedByName = "userIdToUser")
    @Mapping(source = "partyId", target = "party", qualifiedByName = "partyIdToParty")
    Comment commentDtoToComment(CommentPostDto commentDto);

    @Mapping(source = "user", target = "userId", qualifiedByName = "userToUserId")
    @Mapping(source = "party", target = "partyId", qualifiedByName = "partyToPartyId")
    CommentPostDto commentToCommentDto(Comment comment);

    CommentResponseDto commentToCommentResponseDto(Comment comment);

    CommentResponseDto replyToCommentResponseDto(Reply reply);

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
