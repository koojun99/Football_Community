package honajun.football_community.feed.reaction.dto;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReactionResponseDTO {

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class onSuccess {
        private Long targetId;
        private Long reactionId;
        private String reactionMessage;
    }
}
