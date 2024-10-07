package honajun.football_community.feed.post.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PostRequestDTO {

    @Getter
    public static class createPost {
        private Long categoryId;
        private String title;
        private String body;
    }

    @Getter
    public static class updatePost {
        private String title;
        private String body;
    }
}
