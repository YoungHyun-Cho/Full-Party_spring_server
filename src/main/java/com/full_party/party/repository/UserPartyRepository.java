package com.full_party.party.repository;

import com.full_party.party.entity.UserParty;
import com.full_party.party.entity.Waiter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserPartyRepository extends JpaRepository<UserParty, Long> {
    Optional<UserParty> findByUserIdAndPartyId(Long userId, Long partyId);
}
