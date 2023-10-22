package com.full_party.tag.service;

import com.full_party.exception.BusinessLogicException;
import com.full_party.exception.ExceptionCode;
import com.full_party.party.entity.Party;
import com.full_party.tag.entity.Tag;
import com.full_party.tag.repository.TagRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TagService {

    private final TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public ArrayList<Tag> createTagList(Party party, ArrayList<String> tags) {

        ArrayList<Tag> tagList = new ArrayList<>();

        tags.stream()
                .forEach(tagStr -> {
                    Tag tag = new Tag(party, tagStr);
                    tagRepository.save(tag);
                    tagList.add(tag);
                });

        party.setTagList(tagList);

        return tagList;
    }

    public ArrayList<Tag> findTags(Long partyId) {

        List<Tag> tags = tagRepository.findByPartyId(partyId);

        Optional.ofNullable(tags)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.TAGS_NOT_FOUND));

        return (ArrayList<Tag>) tags;
    }
}
