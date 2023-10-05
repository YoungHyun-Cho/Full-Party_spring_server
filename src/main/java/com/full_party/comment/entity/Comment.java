package com.full_party.comment.entity;

import com.full_party.audit.Auditable;
import com.full_party.party.entity.Party;
import com.full_party.user.entity.User;
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
public class Comment extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @ManyToOne
    @JoinColumn(name = "PARTY_ID")
    private Party party;

    @OneToMany(mappedBy = "comment")
    private List<Reply> replies = new ArrayList<>();

    @Column(nullable = false)
    private String content;
}
