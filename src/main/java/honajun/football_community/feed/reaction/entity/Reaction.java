package honajun.football_community.feed.reaction.entity;

import honajun.football_community.feed.comment.entity.Comment;
import honajun.football_community.feed.post.entity.Post;
import honajun.football_community.global.common.BaseDateTimeEntity;
import honajun.football_community.global.enums.reaction.ReactionType;
import honajun.football_community.global.enums.reaction.TargetType;
import honajun.football_community.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class Reaction extends BaseDateTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long targetId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ReactionType reactionType; // like, dislike

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TargetType targetType; // post, comment

    @JoinColumn(name = "reactor_id")
    @ManyToOne
    private Member reactor;

    @JoinColumn(name = "post_id", nullable = true)
    @ManyToOne
    private Post post;

    @JoinColumn(name = "comment_id", nullable = true)
    @ManyToOne
    private Comment comment;
}
