package honajun.football_community.wiki;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WikiRequestDTO {

    @Getter
    public static class createCategory {
        private String name;
        private String description;
    }

    @Getter
    public static class updateWiki {
        private String content;
    }
}