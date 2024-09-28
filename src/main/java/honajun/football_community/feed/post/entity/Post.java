package honajun.football_community.feed.post.entity;

import honajun.football_community.global.common.BaseDateTimeEntity;
import honajun.football_community.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class Post extends BaseDateTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String body;

    @JoinColumn(name = "writer_id")
    @ManyToOne
    private Member writer;

}
