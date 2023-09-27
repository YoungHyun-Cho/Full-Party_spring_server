package com.full_party.party.entity;

import com.full_party.audit.Auditable;
import com.full_party.comment.entity.Comment;
import com.full_party.heart.entity.Heart;
import com.full_party.quest.entity.Quest;
import com.full_party.values.PartyState;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Party extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "QUEST_ID")
    private Quest quest;

    @OneToMany(mappedBy = "party", cascade = CascadeType.PERSIST)
    private List<UserParty> userParties = new ArrayList<>();

    @OneToMany(mappedBy = "party")
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "party")
    private List<Heart> hearts = new ArrayList<>();

    @OneToMany(mappedBy = "party")
    private List<Waiter> waiters = new ArrayList<>();

    @Column(nullable = false)
    private Integer memberLimit;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PartyState partyState;

    public Party(Quest quest, Integer memberLimit, PartyState partyState) {
        this.quest = quest;
        this.memberLimit = memberLimit;
        this.partyState = partyState;
    }

    public Party(Long id) {
        this.id = id;
    }
}
