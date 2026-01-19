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
public class FixturePlayerStatsResponseDTO {

    private Long fixtureId;
    private List<PlayerStats> players;

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class PlayerStats {
        private Long playerId;
        private String playerName;
        private Long teamId;
        private String teamName;
        private String position;
        private Integer minutes;

        // 공격/패스/수비 등 주요 스탯
        private Integer goals;
        private Integer assists;
        private Integer shotsTotal;
        private Integer shotsOnTarget;
        private Integer passesTotal;
        private Integer passesKey;
        private String passesAccuracy; // API-Football에서 문자열(%)로 내려옴
        private Integer tacklesTotal;
        private Integer interceptions;
        private Integer foulsCommitted;
        private Integer foulsDrawn;
        private Integer yellowCards;
        private Integer redCards;
    }
}

