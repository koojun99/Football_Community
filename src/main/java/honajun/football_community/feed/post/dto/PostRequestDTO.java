package honajun.football_community.feed.post.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PostRequestDTO {

    @Getter
    public static class createPost {

        @NotNull(message = "categoryId를 입력해주세요")
        private Long categoryId;

        @NotNull(message = "제목을 입력해주세요")
        private String title;

        @NotNull(message = "내용을 입력해주세요")
        private String body;
    }

    @Getter
    public static class updatePost {

        private String title;

        private String body;
    }
}
