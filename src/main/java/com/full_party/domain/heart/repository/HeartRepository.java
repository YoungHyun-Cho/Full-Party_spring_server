package com.full_party.domain.heart.repository;

import com.full_party.domain.heart.entity.Heart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HeartRepository extends JpaRepository<Heart, Long> {

    List<Heart> findByUserId(Long userId);
    List<Heart> findByPartyId(Long partyId);
    Optional<Heart> findByUserIdAndPartyId(Long userId, Long partyId);
}
