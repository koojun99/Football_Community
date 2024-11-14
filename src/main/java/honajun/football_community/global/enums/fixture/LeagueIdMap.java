package honajun.football_community.global.enums.fixture;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LeagueIdMap {
    PREMIER_LEAGUE(39L, "Premier League", 1L),
    ;

    private final Long externalId;
    private final String leagueName;
    private final Long internalId;

    public static Long toInternalId(Long externalId) {
        for (LeagueIdMap map : values()) {
            if (map.getExternalId() == externalId) {
                return map.getInternalId();
            }
        }
        throw new IllegalArgumentException("Invalid external league ID: " + externalId);
    }

    public static Long toExternalId(Long internalId) {
        for (LeagueIdMap map : values()) {
            if (map.getInternalId() == internalId) {
                return map.getExternalId();
            }
        }
        throw new IllegalArgumentException("Invalid internal league ID: " + internalId);
    }
}
