package com.full_party.party.repository;

import com.full_party.party.entity.Party;
import com.full_party.party.entity.UserParty;
import com.full_party.party.entity.Waiter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserPartyRepository extends JpaRepository<UserParty, Long> {
    Optional<UserParty> findByUserIdAndPartyId(Long userId, Long partyId);
    @Query("SELECT up FROM UserParty up WHERE up.user.id = :userId AND up.party.partyState != 'DISMISSED'")
    List<UserParty> findByUserId(Long userId);
    List<UserParty> findByPartyId(Long partyId);
}
