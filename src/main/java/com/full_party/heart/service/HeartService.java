package com.full_party.heart.service;

import com.full_party.heart.entity.Heart;
import com.full_party.heart.repository.HeartRepository;
import com.full_party.party.entity.Party;
import com.full_party.user.entity.User;
import com.full_party.user.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HeartService {

    private final HeartRepository heartRepository;
    private final UserService userService;

    public HeartService(HeartRepository heartRepository, UserService userService) {
        this.heartRepository = heartRepository;
        this.userService = userService;
    }

    public Heart createHeart(User user, Party party) {
        Heart heart = new Heart(user, party);
        return heartRepository.save(heart);
    }

    public List<Heart> findHearts(Long userId) {
        return heartRepository.findByUserId(userId);
    }

    public void deleteHeart(User user, Party party) {
        Heart heart = new Heart(user, party);
        heartRepository.delete(heart);
    }

    public Boolean checkIsHeart(Long userId, Long partyId) {

        Optional<Heart> optionalHeart = heartRepository.findByUserIdAndPartyId(userId, partyId);
        if (optionalHeart.isPresent()) return true;
        else return false;
    }
}
