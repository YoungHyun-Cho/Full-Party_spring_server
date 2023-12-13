package com.full_party.domain.comment.entity;

import com.full_party.global.audit.Auditable;
import com.full_party.domain.user.entity.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Reply extends Auditable implements QnA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @ManyToOne
    @JoinColumn(name = "COMMENT_ID")
    private Comment comment;

    @Column(nullable = false)
    private String content;
}
