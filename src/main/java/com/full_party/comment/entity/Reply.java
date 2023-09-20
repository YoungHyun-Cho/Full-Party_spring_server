package com.full_party.comment.entity;

import com.full_party.audit.Auditable;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class Reply extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long replyId;

    @Column(nullable = false)
    private String content;
}
