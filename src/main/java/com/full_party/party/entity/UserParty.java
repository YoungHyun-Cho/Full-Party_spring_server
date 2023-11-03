package com.full_party.party.entity;

import com.full_party.audit.Auditable;
import com.full_party.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserParty extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne()
    @JoinColumn(name = "USER_ID")
    private User user;

    @ManyToOne
    @JoinColumn(name = "PARTY_ID")
    private Party party;

    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    private Boolean isReviewed;

    public UserParty(User user, Party party, String message, Boolean isReviewed) {
        this.user = user;
        this.party = party;
        this.message = message;
        this.isReviewed = isReviewed;
    }
}
