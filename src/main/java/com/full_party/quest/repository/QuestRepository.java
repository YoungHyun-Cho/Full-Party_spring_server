package com.full_party.quest.repository;

import com.full_party.quest.entity.Quest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestRepository extends JpaRepository<Quest, Long> {
}
