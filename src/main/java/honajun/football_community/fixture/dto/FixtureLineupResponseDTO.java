package honajun.football_community.fixture.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class FixtureLineupResponseDTO {

    private List<TeamLineup> lineups;

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class TeamLineup {
        private Long teamId;
        private String teamName;
        private String formation;
        private List<Player> starters;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class Player {
        private Long playerId;
        private String playerName;
        private Integer number;
        private String position;
        private String grid;
    }
}

