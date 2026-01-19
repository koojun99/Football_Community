package honajun.football_community.wiki;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WikiRequestDTO {

    @Getter
    public static class createWiki {
        private String title;
        private Long teamId; // 팀 위키인 경우 (선택)
        private Long leagueId; // 리그 위키인 경우 (선택)
    }

    @Getter
    public static class createCategory {
        private String name;
        private String description;
    }

    @Getter
    public static class updateWiki {
        private String name; // 카테고리 이름 (선택)
        private String description; // 카테고리 내용 (선택)
    }
}