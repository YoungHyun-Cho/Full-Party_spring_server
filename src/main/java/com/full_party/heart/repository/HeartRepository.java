package com.full_party.heart.repository;

import com.full_party.heart.entity.Heart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HeartRepository extends JpaRepository<Heart, Long> {

    List<Heart> findByUserId(Long userId);
    Optional<Heart> findByUserIdAndPartyId(Long userId, Long partyId);
}
