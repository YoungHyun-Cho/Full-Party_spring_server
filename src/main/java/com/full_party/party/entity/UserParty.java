package com.full_party.party.entity;

import com.full_party.audit.Auditable;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class UserParty extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userPartyId;

    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    private Boolean isReviewed;
}
