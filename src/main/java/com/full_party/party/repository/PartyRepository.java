package com.full_party.party.repository;

import com.full_party.party.entity.Party;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartyRepository extends JpaRepository<Party, Long> {
}
