package honajun.football_community.member;

import com.fasterxml.jackson.annotation.JsonIgnore;
import honajun.football_community.global.common.BaseDateTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseDateTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column
    private String phoneNumber;

    @Column(nullable = false)
    private String status;

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
