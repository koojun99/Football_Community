package honajun.football_community.member.entity;

import honajun.football_community.global.common.BaseDateTimeEntity;
import honajun.football_community.global.enums.MemberStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Member extends BaseDateTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(unique = true)
    private String phoneNumber;

    @Column
    private String bio;

    @Column(nullable = false)
    @Setter
    private MemberStatus status;

    public void update(String name, String username, String bio, String phoneNumber) {

        if (Objects.nonNull(name)) {
            this.name = name;
        }
        if (Objects.nonNull(username)) {
            this.username = username;
        }
        if (Objects.nonNull(bio)) {
            this.bio = bio;
        }
        if (Objects.nonNull(phoneNumber)) {
            this.phoneNumber = phoneNumber;
        }
    }

    public void withdraw() {
        this.status = MemberStatus.WITHDRAWAL;
    }

//    @JsonIgnore
//    @OneToOne(cascade = CascadeType.ALL, mappedBy = "member")
//    private Newsfeed newsfeed;

//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "author")
//    private List<Post> posts;

//    @JsonIgnore
//    @OneToMany(mappedBy = "user")
//    private List<Like> likes;

//    @JsonIgnore
//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "author")
//    private List<MatchReview> reviews;

}