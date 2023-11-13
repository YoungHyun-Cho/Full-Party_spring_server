package com.full_party.comment.mapper;

import com.full_party.comment.dto.CommentPostDto;
import com.full_party.comment.dto.CommentResponseDto;
import com.full_party.comment.dto.ReplyPostDto;
import com.full_party.comment.entity.Comment;
import com.full_party.comment.entity.QnA;
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

    @Mapping(source = "userId", target = "user", qualifiedByName = "userIdToUser")
    @Mapping(source = "commentId", target = "comment", qualifiedByName = "commentIdToComment")
    Reply replyPostDtoToReply(ReplyPostDto replyPostDto);

//    @Mapping(source = "user", target = "userId", qualifiedByName = "userToUserId")
//    @Mapping(source = "comment", target = "commentId", qualifiedByName = "commentToCommentId")

    default CommentResponseDto mapToCommentResponseDto(QnA qna) {
        return new CommentResponseDto(
                qna.getId(),
                qna.getUser().getId(),
                qna.getUser().getUserName(),
                qna.getUser().getProfileImage(),
                qna.getContent(),
                qna.getCreatedAt()
        );
    };

    @Named("userIdToUser")
    default User userIdToUser(Long userId) {
        return new User(userId);
    }

    @Named("partyIdToParty")
    default Party partyIdToParty(Long partyId) {
        return new Party(partyId);
    }

    @Named("commentIdToComment")
    default Comment commentIdToComment(Long commentId) {
        return new Comment(commentId);
    }

    @Named("userToUserId")
    default Long userToUserId(User user) {
        return user.getId();
    }

    @Named("partyToPartyId")
    default Long partyToPartyId(Party party) {
        return party.getId();
    }

    @Named("commentToCommentId")
    default Long commentToCommentId(Comment comment) {
        return comment.getId();
    }
}
