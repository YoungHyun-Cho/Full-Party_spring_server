package com.full_party.quest.service;

import com.full_party.exception.BusinessLogicException;
import com.full_party.exception.ExceptionCode;
import com.full_party.party.repository.PartyRepository;
import com.full_party.party.repository.UserPartyRepository;
import com.full_party.quest.entity.Quest;
import com.full_party.quest.repository.QuestRepository;
import com.full_party.tag.entity.Tag;
import com.full_party.tag.service.TagService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class QuestService {

    private final QuestRepository questRepository;
    private final UserPartyRepository userPartyRepository;
    private final PartyRepository partyRepository;
    private final TagService tagService;

    public QuestService(QuestRepository questRepository, UserPartyRepository userPartyRepository, PartyRepository partyRepository, TagService tagService) {
        this.questRepository = questRepository;
        this.userPartyRepository = userPartyRepository;
        this.partyRepository = partyRepository;
        this.tagService = tagService;
    }

    public Quest createQuest(Quest quest, ArrayList<String> tags) {

        // 🟥 Tag 연관 관계 설정
        ArrayList<Tag> tagList = tagService.createTagList(quest, tags);
        quest.setTagList(tagList);

        // 🟥 Party 생성 필요

        return questRepository.save(quest);
    }

    public Quest findQuest(Long questId) {
        return findVerifiedQuest(questId);
    }

//    public ArrayList<Quest> findQuests() {
//        // userParty 테이블에서 userId가 일치하는 파티의 partyId와 연관된 quest 조회
//        // region이 일치하는 quest 조회
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
