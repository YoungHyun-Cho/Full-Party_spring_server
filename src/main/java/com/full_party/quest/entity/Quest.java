package com.full_party.quest.entity;

import com.full_party.audit.Auditable;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
public class Quest extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long questId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String image;

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column(nullable = false)
    private LocalDateTime endDate;

    @Column(nullable = false)
    private Boolean isOnline;

    @Column(nullable = false)
    private String privateLink;

    @Column(nullable = false)
    private String region;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private String coordinates; // 직렬화 필요
}
