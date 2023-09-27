package com.full_party.party.repository;

import com.full_party.party.entity.Waiter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WaiterRepository extends JpaRepository<Waiter, Long> {
    Optional<Waiter> findByUserIdAndPartyId(Long userId, Long partyId);
}
