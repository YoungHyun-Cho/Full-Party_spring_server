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
import java.util.stream.Collectors;

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

//    public List<Tag> updateTagList(Party party, List<String> newTagStrList) {

//        party.getTagList().stream()
//                .forEach(tag -> deleteTag(tag));

//        return createTagList(party, newTagStrList);
//    }
 }
