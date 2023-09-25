package com.full_party.quest.entity;

import com.full_party.audit.Auditable;
import com.full_party.party.entity.Party;
import com.full_party.tag.entity.Tag;
import com.full_party.user.entity.User;
import com.full_party.values.Coordinates;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Quest extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 퀘스트 게시자
    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @OneToOne
    @JoinColumn(name = "PARTY_ID")
    private Party party;

    @OneToMany(mappedBy = "quest", cascade = CascadeType.PERSIST)
    private List<Tag> tagList = new ArrayList<>();

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

    @Embedded
    @Column(nullable = false)
    private Coordinates coordinates;

    public Quest(Quest quest) {
        this.id = quest.id;
        this.name = quest.name;
        this.image = quest.image;
        this.startDate = quest.startDate;
        this.endDate = quest.endDate;
        this.isOnline = quest.isOnline;
        this.privateLink = quest.privateLink;
        this.region = quest.region;
        this.location = quest.location;
        this.coordinates = quest.coordinates;
    }
}
