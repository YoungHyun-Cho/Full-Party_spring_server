package com.full_party.quest.service;

import com.full_party.exception.BusinessLogicException;
import com.full_party.exception.ExceptionCode;
import com.full_party.party.entity.Party;
import com.full_party.party.repository.PartyRepository;
import com.full_party.party.repository.UserPartyRepository;
import com.full_party.party.service.PartyService;
import com.full_party.quest.entity.Quest;
import com.full_party.quest.repository.QuestRepository;
import com.full_party.tag.entity.Tag;
import com.full_party.tag.service.TagService;
import com.full_party.user.repository.UserRepository;
import com.full_party.user.service.UserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class QuestService {

    private final QuestRepository questRepository;
    private final PartyService partyService;
    private final TagService tagService;

    public QuestService(QuestRepository questRepository, PartyService partyService, TagService tagService) {
        this.questRepository = questRepository;
        this.partyService = partyService;
        this.tagService = tagService;
    }

    public Quest createQuest(Quest quest, Party party, ArrayList<Tag> tagList) {
        quest.setParty(party);
        quest.setTagList(tagList);
        return questRepository.save(quest);
    }

    public Quest findQuest(Long questId) {

        Quest foundQuest = findVerifiedQuest(questId);

        ArrayList<Tag> tagList = tagService.findTags(questId);

        foundQuest.setTagList(tagList);

        return foundQuest;
    }

    public List<Quest> findLocalQuests(String region) {

        return questRepository.findByRegion(region);
    }

//
//    public ArrayList<Quest> findMyQuests(Long userId) {
//
//    }
//
//    public ArrayList<Quest> findLocalQuests(String Region) {
//
//    }

    public Quest updateQuest(Quest quest) {
        Quest updatedQuest = new Quest(quest);
        return questRepository.save(updatedQuest);
    }

    public void deleteQuest(Long questId) {
        Quest quest = findVerifiedQuest(questId);
        questRepository.delete(quest);
    }

    private Quest findVerifiedQuest(Long questId) {
        Optional<Quest> optionalQuest = questRepository.findById(questId);
        Quest quest = optionalQuest.orElseThrow(() -> new BusinessLogicException(ExceptionCode.QUEST_NOT_FOUND));
        return quest;
    }
}
