package honajun.football_community.wiki.dto;

import java.util.List;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WikiResponseDTO {

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class getWiki {
        private Long id;
        private String title;
        List<getCategory> categories;
    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class getCategory {
        private Long id;
        private String name;
        private String description;
        private Integer orderIndex; // 목차 순서
    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class getWikiList {
        private List<getWiki> wikis;
    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class getWikiId {
        private Long id; // 프론트엔드에서 response.data.id로 접근하기 위해 id 필드 사용
    }
}