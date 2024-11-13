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
    }
}