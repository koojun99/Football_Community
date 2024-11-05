package honajun.football_community.team.dto;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TeamResponseDTO {

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class getTeamWiki {
        private Long id;
        private String name;
        private String founded;
        private String stadium;
        private String headCoach;
        private Long leagueId;
    }
}