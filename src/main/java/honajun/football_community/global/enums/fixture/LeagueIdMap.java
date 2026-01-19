package honajun.football_community.global.enums.fixture;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LeagueIdMap {
    PREMIER_LEAGUE(39L, "Premier League"),
    ;

    private final Long leagueId; // API-Football에서 사용하는 ID
    private final String leagueName;
}
