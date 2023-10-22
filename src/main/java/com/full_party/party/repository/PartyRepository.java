package com.full_party.party.repository;

import com.full_party.party.entity.Party;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PartyRepository extends JpaRepository<Party, Long> {
        Optional<Party> findById(Long id);
        List<Party> findByRegion(String region);
        List<Party> findByUserId(Long userId);
}
