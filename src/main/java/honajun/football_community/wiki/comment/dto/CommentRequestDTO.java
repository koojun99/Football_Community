package honajun.football_community.wiki.comment.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentRequestDTO {

    @Getter
    public static class createComment {
        private String body;
    }

    @Getter
    public static class updateComment {
        private String body;
    }
}

