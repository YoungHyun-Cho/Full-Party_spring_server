package com.full_party.tag.entity;

import com.full_party.audit.Auditable;
import com.full_party.party.entity.Party;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class Tag extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tagId;

    @ManyToOne
    @JoinColumn(name = "PARTY_ID")
    private Party party;

    @Column(nullable = false)
    private String name;
}
