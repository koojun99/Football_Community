package honajun.football_community.league.dto;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LeagueResponseDTO {

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class getLeagueWiki {
        private Long id;
        private Integer division;
        private String name;
        private String country;
        private String founded;
        private String mostRecentChampion;
        private String mostWinningTeam;
    }
}