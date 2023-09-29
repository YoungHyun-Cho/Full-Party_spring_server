package com.full_party.heart.entity;

import com.full_party.audit.Auditable;
import com.full_party.party.entity.Party;
import com.full_party.user.entity.User;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class Heart extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @ManyToOne
    @JoinColumn(name = "PARTY_ID")
    private Party party;

    public Heart(User user, Party party) {
        this.user = user;
        this.party = party;
    }
}
