package honajun.football_community.feed.reaction.dto;

import honajun.football_community.global.enums.reaction.ReactionType;
import honajun.football_community.global.enums.reaction.TargetType;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReactionRequestDTO {

    @Getter
    public static class createReaction {

        @NotNull(message = "targetType을 입력해주세요(POST, COMMENT)")
        private TargetType targetType;

        @NotNull(message = "reactionType을 입력해주세요(LIKE, DISLIKE)")
        private ReactionType reactionType;
    }
}
