package honajun.football_community.global.enums.reaction;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
public enum ReactionType {
    LIKE("좋아요"),
    DISLIKE("싫어요");

    @Getter
    private final String message;

}
