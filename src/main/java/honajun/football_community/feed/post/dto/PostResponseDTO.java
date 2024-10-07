package honajun.football_community.feed.post.dto;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PostResponseDTO {

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class getPostId {
        private Long postId;
    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class postDetail {
        private Long postId;
        private String title;
        private String body;
        private Long writerId;
        private String writerName;
    }
}
