package com.full_party.domain.comment.entity;

import com.full_party.global.audit.Auditable;
import com.full_party.domain.party.entity.Party;
import com.full_party.domain.user.entity.User;
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
@AllArgsConstructor
@NoArgsConstructor
public class Comment extends Auditable implements QnA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @ManyToOne
    @JoinColumn(name = "PARTY_ID")
    private Party party;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.REMOVE)
    private List<Reply> replies = new ArrayList<>();

    @Column(nullable = false)
    private String content;

    public Comment(Long id) {
        this.id = id;
    }
}
