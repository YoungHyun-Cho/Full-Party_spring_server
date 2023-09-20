package com.full_party.party.repository;

import com.full_party.party.entity.UserParty;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPartyRepository extends JpaRepository<UserParty, Long> {
}
