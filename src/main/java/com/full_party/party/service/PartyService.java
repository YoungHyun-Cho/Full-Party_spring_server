package com.full_party.party.service;

import com.full_party.exception.BusinessLogicException;
import com.full_party.exception.ExceptionCode;
import com.full_party.party.entity.Party;
import com.full_party.party.entity.UserParty;
import com.full_party.party.entity.Waiter;
import com.full_party.party.repository.PartyRepository;
import com.full_party.party.repository.UserPartyRepository;
import com.full_party.party.repository.WaiterRepository;
import com.full_party.quest.entity.Quest;
import com.full_party.user.entity.User;
import com.full_party.user.service.UserService;
import com.full_party.values.PartyState;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PartyService {
    private final PartyRepository partyRepository;
    private final UserPartyRepository userPartyRepository;
    private final WaiterRepository waiterRepository;
    private final UserService userService;

    public PartyService(PartyRepository partyRepository, UserPartyRepository userPartyRepository, WaiterRepository waiterRepository, UserService userService) {
        this.partyRepository = partyRepository;
        this.userPartyRepository = userPartyRepository;
        this.waiterRepository = waiterRepository;
        this.userService = userService;
    }

    public Party createParty(Quest quest, Integer memberLimit) {
        Party party = new Party(quest, memberLimit, PartyState.RECRUITING);
        return partyRepository.save(party);
    }

    public Party findParty(Long partyId) {
        return findVerifiedParty(partyId);
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

    private Waiter findVerifiedWaiter(Long userId, Long partyId) {
        Optional<Waiter> optionalWaiter = waiterRepository.findByUserIdAndPartyId(userId, partyId);
        Waiter waiter = optionalWaiter.orElseThrow(() -> new BusinessLogicException(ExceptionCode.WAITER_NOT_FOUND));
        return waiter;
    }

    public UserParty createUserParty(Long userId, Long partyId) {

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


}
