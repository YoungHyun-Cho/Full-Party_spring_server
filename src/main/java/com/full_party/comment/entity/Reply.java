package com.full_party.comment.entity;

import com.full_party.audit.Auditable;
import com.full_party.user.entity.User;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class Reply extends Auditable {

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
