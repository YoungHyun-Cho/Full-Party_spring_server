package com.full_party.domain.tag.service;

import com.full_party.domain.party.entity.Party;
import com.full_party.domain.tag.entity.Tag;
import com.full_party.domain.tag.repository.TagRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TagService {

    private final TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public List<Tag> createTagList(Party party, List<String> tags) {

        List<Tag> tagList = new ArrayList<>();

        tags.stream()
                .forEach(tagStr -> tagList.add(createTag(party, tagStr)));

        return tagList;
    }

    public Tag createTag(Party party, String tagStr) {

        Tag tag = new Tag(party, tagStr);
        return tagRepository.save(tag);
    }

    private void deleteTag(Tag tag) {
        tagRepository.delete(tag);
    }

    public void deleteAllTag(Party party) {
        party.getTagList().stream()
                .forEach(tag -> deleteTag(tag));
    }
 }
