package com.full_party.party.entity;

import com.full_party.audit.Auditable;
import com.full_party.comment.entity.Comment;
import com.full_party.heart.entity.Heart;
import com.full_party.tag.entity.Tag;
import com.full_party.user.entity.User;
import com.full_party.values.Coordinates;
import com.full_party.values.PartyState;
import com.full_party.values.SignUpType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Party extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String image;

    @Column(nullable = false)
    private String content;

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
    private Integer memberLimit;

    @Embedded
    @Column(nullable = false)
    private Coordinates coordinates;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PartyState partyState = PartyState.RECRUITING;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @OneToMany(mappedBy = "party", cascade = CascadeType.ALL, orphanRemoval = true)
//    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Tag> tagList = new ArrayList<>();

    @OneToMany(mappedBy = "party", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserParty> userParties = new ArrayList<>();

    @OneToMany(mappedBy = "party", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "party", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Heart> hearts = new ArrayList<>();

    @OneToMany(mappedBy = "party", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Waiter> waiters = new ArrayList<>();

    @Transient
    private Boolean isHeart; // 임시 값 저장을 위한 필드 -> transient

    @Transient
    private Boolean isReviewed;

    @Transient
    private Integer heartCount;

    @Transient
    private List<PartyMember> memberList; // 파티원 리스트

    @Transient
    private List<PartyMember> waiterList; // 대기자 리스트

    /*
    * @Transient 어노테이션은 JPA에서 엔티티 클래스의 특정 필드를 데이터베이스 테이블의 칼럼으로 인식하지 않도록 하는 데 사용됩니다. 즉, 이 어노테이션을 사용하면 해당 필드는 데이터베이스에 저장되지 않습니다. 대신, 일시적인 데이터나 계산된 값과 같이 영속성을 유지할 필요가 없는 필드에 대해 사용됩니다.
    * 주요 목적은 다음과 같습니다:
    * 데이터베이스에 저장하지 않아야 하는 일시적인 데이터를 나타낼 때 사용됩니다. 예를 들어, 비즈니스 계산을 위한 임시 결과 값이나, UI 표시를 위한 보조 데이터 등이 여기에 해당됩니다.
    * 엔티티 클래스의 특정 필드를 데이터베이스 테이블의 칼럼으로 인식하지 않도록 하여, 영속성 작업에 영향을 주지 않게 합니다. 이는 엔티티의 상태를 관리하는 데 유용합니다.
    * 데이터베이스 스키마를 더 유연하게 관리할 수 있게 합니다. 필드를 데이터베이스에 저장하지 않고 엔티티 클래스 내에만 유지할 수 있기 때문에, 데이터베이스 스키마를 변경하지 않고도 필드를 추가하거나 제거할 수 있습니다.
    * 따라서, @Transient 어노테이션은 데이터베이스와의 연관성을 가지지 않는 필드를 엔티티 클래스 내에서 표시하기 위해 사용됩니다. 데이터베이스에 영구적으로 저장할 필요가 없는 일시적인 데이터나 계산된 값에 이 어노테이션을 적용할 수 있습니다.
    * */

    public Party(Long id) {
        this.id = id;
    }

    public Party(Party previousParty, Party modifiedParty) {
        this.id = previousParty.getId();
        this.name = modifiedParty.getName();
        this.image = modifiedParty.getImage();
        this.content = modifiedParty.getContent();
        this.startDate = modifiedParty.getStartDate();
        this.endDate = modifiedParty.getEndDate();
        this.isOnline = modifiedParty.getIsOnline();
        this.privateLink = modifiedParty.getPrivateLink();
        this.region = modifiedParty.getRegion();
        this.location = modifiedParty.getLocation();
        this.memberLimit = modifiedParty.getMemberLimit();
        this.coordinates = modifiedParty.getCoordinates();
        this.partyState = previousParty.getPartyState();
        this.user = previousParty.getUser();
        this.tagList = modifiedParty.tagList;
        this.userParties = previousParty.userParties;
        this.comments = previousParty.getComments();
        this.hearts = previousParty.getHearts();
        this.waiters = previousParty.getWaiters();
        this.isHeart = previousParty.isHeart;
        this.memberList = previousParty.memberList;
        this.waiterList = previousParty.waiterList;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PartyMember {

        private Long id;
        private String userName;
        private String address;
        private String profileImage;
        private String message;
        private Integer exp;
        private Integer level;
        private SignUpType signUpType;
        private LocalDateTime joinDate;

        public PartyMember(User user) {
            this.id = user.getId();
            this.userName = user.getUserName();
            this.address = user.getAddress();
            this.profileImage = user.getProfileImage();
            this.exp = user.getExp();
            this.level = user.getLevel();
            this.signUpType = user.getSignUpType();
        }
    }

}
