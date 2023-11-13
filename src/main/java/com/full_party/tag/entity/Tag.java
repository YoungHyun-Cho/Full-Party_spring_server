package com.full_party.tag.entity;

import com.full_party.audit.Auditable;
import com.full_party.party.entity.Party;
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

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "PARTY_ID")
    private Party party;

    @Column(nullable = false)
    private String value;

    public Tag(Party party, String value) {
        this.party = party;
        this.value = value;
    }
}
