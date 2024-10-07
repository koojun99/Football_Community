package honajun.football_community.feed.reaction.repository;

import honajun.football_community.feed.reaction.entity.Reaction;
import honajun.football_community.global.enums.reaction.ReactionType;
import honajun.football_community.global.enums.reaction.TargetType;
import honajun.football_community.member.entity.Member;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReactionRepository extends JpaRepository<Reaction, Long> {
    boolean existsByReactorAndTargetIdAndTargetTypeAndReactionType(Member reactor, Long targetId, @NotNull(message = "targetType을 입력해주세요(POST, COMMENT)") TargetType targetType, @NotNull(message = "reactionType을 입력해주세요(LIKE, DISLIKE)") ReactionType reactionType);

    Reaction findByReactorAndTargetIdAndTargetTypeAndReactionType(Member reactor, Long targetId, @NotNull(message = "targetType을 입력해주세요(POST, COMMENT)") TargetType targetType, @NotNull(message = "reactionType을 입력해주세요(LIKE, DISLIKE)") ReactionType reactionType);
}
