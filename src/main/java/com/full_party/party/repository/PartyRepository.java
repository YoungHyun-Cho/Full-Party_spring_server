package com.full_party.party.repository;

import com.full_party.party.entity.Party;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PartyRepository extends JpaRepository<Party, Long> {
        Optional<Party> findById(Long id);

        @Query("SELECT p FROM Party p WHERE p.region = :region AND p.partyState != 'DISMISSED' AND p.partyState != 'COMPLETED'")
        List<Party> findByRegion(String region);

        @Query("SELECT p FROM Party p WHERE p.user.id = :userId AND p.partyState != 'DISMISSED'")
        List<Party> findByUserId(Long userId);

        @Query("SELECT p FROM Party p WHERE p.name LIKE %:keyword% AND p.region = :region AND p.partyState != 'DISMISSED' AND p.partyState != 'COMPLETED'")
        List<Party> searchPartiesByKeyword(String keyword, String region);

        @Query("SELECT DISTINCT p FROM Party p JOIN p.tagList t WHERE t.value = :tagValue AND p.region = :region AND p.partyState != 'DISMISSED' AND p.partyState != 'COMPLETED'")
//        @Query("SELECT DISTINCT p FROM Party p JOIN FETCH p.tagList t WHERE t.value = :tagValue AND p.region = :region AND p.partyState != 'DISMISSED' AND p.partyState != 'COMPLETED'")
        List<Party> searchPartiesByTagValue(String tagValue, String region);
}
