package com.full_party.tag.entity;

import com.full_party.audit.Auditable;
import com.full_party.quest.entity.Quest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Tag extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "QUEST_ID")
    private Quest quest;

    @Column(nullable = false)
    private String value;

    public Tag(Quest quest, String value) {
        this.quest = quest;
        this.value = value;
    }
}
