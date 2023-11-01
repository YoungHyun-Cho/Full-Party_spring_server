package com.full_party.party.service;

import com.full_party.comment.service.CommentService;
import com.full_party.exception.BusinessLogicException;
import com.full_party.exception.ExceptionCode;
import com.full_party.heart.service.HeartService;
import com.full_party.party.entity.Party;
import com.full_party.party.entity.PartyMember;
import com.full_party.party.entity.UserParty;
import com.full_party.party.entity.Waiter;
import com.full_party.party.mapper.PartyMapper;
import com.full_party.party.repository.PartyRepository;
import com.full_party.party.repository.UserPartyRepository;
import com.full_party.party.repository.WaiterRepository;
import com.full_party.user.entity.User;
import com.full_party.user.service.UserService;
import com.full_party.values.PartyState;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class PartyService {
    private final PartyRepository partyRepository;
    private final UserPartyRepository userPartyRepository;
    private final WaiterRepository waiterRepository;
    private final UserService userService;
    private final HeartService heartService;
    private final CommentService commentService;

    public PartyService(PartyRepository partyRepository, UserPartyRepository userPartyRepository, WaiterRepository waiterRepository, UserService userService, HeartService heartService, CommentService commentService) {
        this.partyRepository = partyRepository;
        this.userPartyRepository = userPartyRepository;
        this.waiterRepository = waiterRepository;
        this.userService = userService;
        this.heartService = heartService;
        this.commentService = commentService;
    }

    public Party createParty(Party party, User user) {
        party.setUser(user);
        return partyRepository.save(party);
    }

    public List<Party> findLeadingParty(Long userId) {
        return partyRepository.findByUserId(userId);
    }

    public List<Party> findParticipatingParty(Long userId) {
        return userPartyRepository.findByUserId(userId).stream()
                .map(userParty -> findParty(userParty.getParty().getId()))
                .collect(Collectors.toList());
    }

    public List<Party> findProgressingMyParty(Long userId) {
        return findMyParties(userId).stream()
                .filter(party -> party.getPartyState() != PartyState.COMPLETED)
                .collect(Collectors.toList());
    }

    public List<Party> findCompletedMyParty(Long userId) {
        return findMyParties(userId).stream()
                .filter(party -> party.getPartyState() == PartyState.COMPLETED)
                .collect(Collectors.toList());
    }

    // 내가 파티장인 파티와 파티원인 파티를 모두 취합해서 리턴
    public List<Party> findMyParties(Long userId) {
        return Stream.of(findLeadingParty(userId), findParticipatingParty(userId))
                .flatMap(el -> el.stream())
                .collect(Collectors.toList());
    }

    public List<Party> findLocalParties(Long userId, String region) {

        List<Party> localParties = partyRepository.findByRegion(region);

        setIsHeart(userId, localParties);
        setMembers(localParties);

        return localParties;
    }

    private void setIsHeart(Long userId, List<Party> partyList) {

        partyList.stream()
                .forEach(party -> party.setIsHeart(heartService.checkIsHeart(userId, party.getId())));
    }

    private void setMembers(List<Party> partyList) {
        // getPartyMembers => partyId를 받아 파티장을 포함한 파티 전체 멤버를 리스트로 리턴

        partyList.stream()
                .forEach(party -> party.setMemberList(findPartyMembers(party)));
    }

    private List<PartyMember> findPartyMembers(Party party) {

        PartyMember leader = new PartyMember(party.getUser());
        leader.setJoinDate(party.getCreatedAt());

        List<PartyMember> members = userPartyRepository.findByPartyId(party.getId()).stream()
                .map(userParty -> {
                    PartyMember partyMember = new PartyMember(userService.findUser(userParty.getUser().getId()));
                    partyMember.setJoinDate(findUserParty(partyMember.getId(), party.getId()).getCreatedAt());
                    return partyMember;
                })
                .collect(Collectors.toList());

        members.add(0, leader);

        return members;
    }

    public Party findParty(Long partyId) {

        Party party = findVerifiedParty(partyId);
        party.setMemberList(findPartyMembers(party));
        party.setWaiterList(findWaiters(party));

        return party;
    }

    public Party updateParty(Party party) {
        Party foundParty = findVerifiedParty(party.getId());

        foundParty.setPartyState(party.getPartyState());
        foundParty.setMemberLimit(party.getMemberLimit());

        return partyRepository.save(foundParty);
    }

    private Party findVerifiedParty(Long partyId) {
        Optional<Party> optionalParty = partyRepository.findById(partyId);
        Party party = optionalParty.orElseThrow(() -> new BusinessLogicException(ExceptionCode.PARTY_EXISTS));
        return party;
    }

//    public Waiter createWaiter(Waiter waiter) {
//
//        User user = userService.findUser(waiter.getUser().getId());
//        Party party = findParty(waiter.getParty().getId());
//
//        waiter.setUser(user);
//        waiter.setParty(party);
//
//        return waiterRepository.save(waiter);
//    }

    public Waiter createWaiter(Long userId, Long partyId, String message) {

        User user = userService.findUser(userId);
        Party party = findParty(partyId);
        Waiter waiter = new Waiter(user, party, message);

        return waiterRepository.save(waiter);
    }

//    public Waiter updateWaiterMessage(Waiter waiter) {
//        Waiter foundWaiter = findWaiter(waiter.getUser().getId(), waiter.getParty().getId());
//        foundWaiter.setMessage(waiter.getMessage());
//        return waiterRepository.save(foundWaiter);
//    }

    public Waiter updateWaiterMessage(Long userId, Long partyId, String message) {
        Waiter foundWaiter = findWaiter(userId, partyId);
        foundWaiter.setMessage(message);
        return waiterRepository.save(foundWaiter);
    }

    private Waiter findWaiter(Long userId, Long partyId) {
        return findVerifiedWaiter(userId, partyId);
    }

    private List<User> findWaiters(Party party) {

        return waiterRepository.findByPartyId(party.getId()).stream()
                .map(waiter -> userService.findUser(waiter.getUser().getId()))
                .collect(Collectors.toList());
    }

    private Waiter findVerifiedWaiter(Long userId, Long partyId) {
        Optional<Waiter> optionalWaiter = waiterRepository.findByUserIdAndPartyId(userId, partyId);
        Waiter waiter = optionalWaiter.orElseThrow(() -> new BusinessLogicException(ExceptionCode.WAITER_NOT_FOUND));
        return waiter;
    }

    public UserParty createUserParty(Long userId, Long partyId) {

        System.out.println("🟥 userId:" + userId + "/ partyId:" + partyId);
        Waiter foundWaiter = findWaiter(userId, partyId);


        UserParty userParty = new UserParty(
                foundWaiter.getUser(),
                foundWaiter.getParty(),
                foundWaiter.getMessage(),
                false
        );

        deleteWaiter(foundWaiter);

        return userPartyRepository.save(userParty);
    }

    public void deleteWaiter(Long userId, Long partyId) {
        Waiter foundWaiter = findWaiter(userId, partyId);
        deleteWaiter(foundWaiter);
    }

    public void deleteWaiter(Waiter waiter) {
        waiterRepository.delete(waiter);
    }

    private UserParty findUserParty(Long userId, Long partyId) {
        return findVerifiedUserParty(userId, partyId);
    }

    private UserParty findVerifiedUserParty(Long userId, Long partyId) {
        Optional<UserParty> optionalUserParty = userPartyRepository.findByUserIdAndPartyId(userId, partyId);
        UserParty userParty = optionalUserParty.orElseThrow(() -> new BusinessLogicException(ExceptionCode.USER_PARTY_NOT_FOUND));
        return userParty;
    }

    public void deleteUserParty(Long userId, Long partyId) {
        UserParty foundUserParty = findUserParty(userId, partyId);
        deleteUserParty(foundUserParty);
    }

    public void deleteUserParty(UserParty userParty) {
        userPartyRepository.delete(userParty);
    }

//    public Boolean CheckIsLeader(User user, Party party) {
//        // 유저가 파티장인지 파티원인지 확인 -> 참여신청 거절 및 강퇴 등에 사용 예정
//    }
}
