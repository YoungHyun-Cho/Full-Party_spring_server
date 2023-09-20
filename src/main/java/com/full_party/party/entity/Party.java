package com.full_party.party.entity;

import com.full_party.audit.Auditable;
import com.full_party.values.Coordinates;
import com.full_party.values.PartyState;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class Party extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long partyId;

    @Column(nullable = false)
    private Integer memberLimit;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PartyState partyState;
}
