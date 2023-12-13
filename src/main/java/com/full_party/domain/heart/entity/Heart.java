package com.full_party.domain.heart.entity;

import com.full_party.global.audit.Auditable;
import com.full_party.domain.user.entity.User;
import com.full_party.domain.party.entity.Party;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
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
