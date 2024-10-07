package honajun.football_community.feed.comment.dto;

import lombok.*;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentResponseDTO {

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class getComment {
        private Long commentId;
        private String body;
        private Long writerId;
        private String writerName;
    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class getCommentList{
        private List<getComment> comments;
    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class getCommentId {
        private Long commentId;
    }
}
