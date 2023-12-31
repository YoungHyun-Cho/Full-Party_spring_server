package com.full_party.domain.heart.service;

import com.full_party.domain.user.entity.User;
import com.full_party.domain.heart.entity.Heart;
import com.full_party.domain.heart.repository.HeartRepository;
import com.full_party.domain.party.entity.Party;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HeartService {

    private final HeartRepository heartRepository;

    public Heart createHeart(User user, Party party) {
        Heart heart = new Heart(user, party);
        return heartRepository.save(heart);
    }

    public Heart findHeart(Long userId, Long partyId) {

        Optional<Heart> optionalHeart = heartRepository.findByUserIdAndPartyId(userId, partyId);

        if (optionalHeart.isPresent()) return optionalHeart.get();
        else return null;
    }

    public List<Heart> findHearts(User user) {
        return heartRepository.findByUserId(user.getId());
    }

    public List<Heart> findHearts(Party party) {
        return heartRepository.findByPartyId(party.getId());
    }

    public void deleteHeart(User user, Party party) {

        heartRepository.delete(findHeart(user.getId(), party.getId()));
    }

    public Boolean checkIsHeart(Long userId, Long partyId) {

        Optional<Heart> optionalHeart = heartRepository.findByUserIdAndPartyId(userId, partyId);
        if (optionalHeart.isPresent()) return true;
        else return false;
    }
}
