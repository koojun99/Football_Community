package honajun.football_community.global.enums.fixture;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TeamIdMap {
    MANCHSETER_UNITED(33L, "Manchester United"),
    LIVERPOOL(40L, "Liverpool"),
    CHELSEA(49L, "Chelsea"),
    ARSENAL(42L, "Arsenal"),
    TOTTENHAM(47L, "Tottenham"),
    MANCHESTER_CITY(50L, "Manchester City"),
    ;

    private final Long teamId; // API-Football에서 사용하는 ID
    private final String teamName;
}
