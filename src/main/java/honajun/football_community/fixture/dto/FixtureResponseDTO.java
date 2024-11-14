package honajun.football_community.fixture.dto;

import honajun.football_community.global.enums.fixture.FixtureStatus;
import java.time.LocalDateTime;
import java.util.List;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FixtureResponseDTO {

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class getFixture {
        private Long fixtureId;
        private String league;
        private String homeTeam;
        private String awayTeam;
        private getScores scores;
        private LocalDateTime fixtureDate;
        private FixtureStatus fixtureStatus;
    }


    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class getScores {
        private Integer homeTeamScore;
        private Integer awayTeamScore;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class getFixtures {
        private List<getFixture> fixtures;
    }

}