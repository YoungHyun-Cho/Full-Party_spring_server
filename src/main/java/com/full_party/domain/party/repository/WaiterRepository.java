package com.full_party.domain.party.repository;

import com.full_party.domain.party.entity.Waiter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WaiterRepository extends JpaRepository<Waiter, Long> {
    Optional<Waiter> findByUserIdAndPartyId(Long userId, Long partyId);
    List<Waiter> findByPartyId(Long partyId);
}
