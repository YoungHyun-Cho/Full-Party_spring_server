package com.full_party.heart.service;

import com.full_party.heart.entity.Heart;
import com.full_party.heart.repository.HeartRepository;
import com.full_party.party.entity.Party;
import com.full_party.party.service.PartyService;
import com.full_party.user.entity.User;
import com.full_party.user.service.UserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HeartService {

    private final HeartRepository heartRepository;
    private final UserService userService;
    private final PartyService partyService;

    public HeartService(HeartRepository heartRepository, UserService userService, PartyService partyService) {
        this.heartRepository = heartRepository;
        this.userService = userService;
        this.partyService = partyService;
    }

    public Heart createHeart(Long userId, Long partyId) {
        Heart heart = makeHeartEntity(userId, partyId);
        return heartRepository.save(heart);
    }

    public List<Heart> findHearts(Long userId) {
        return heartRepository.findByUserId(userId);
    }

    public void deleteHeart(Long userId, Long partyId) {
        Heart heart = makeHeartEntity(userId, partyId);
        heartRepository.delete(heart);
    }

    private Heart makeHeartEntity(Long userId, Long partyId) {
        User user = userService.findUser(userId);
        Party party = partyService.findParty(partyId);
        return new Heart(user, party);
    }
}
