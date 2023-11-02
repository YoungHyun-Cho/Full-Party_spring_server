package com.full_party.tag.repository;

import com.full_party.party.entity.Party;
import com.full_party.tag.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Long> {

    List<Tag> findByPartyId(Long partyId);

    @Query("SELECT t FROM Tag t WHERE t.value LIKE %?1%")
    List<Tag> searchTagsByTagValue(String tagValue);
}
