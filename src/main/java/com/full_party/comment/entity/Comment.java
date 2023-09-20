package com.full_party.comment.entity;

import com.full_party.audit.Auditable;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class Comment extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @Column(nullable = false)
    private String content;
}
