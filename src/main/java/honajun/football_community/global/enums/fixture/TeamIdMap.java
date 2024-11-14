package honajun.football_community.global.enums.fixture;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TeamIdMap {
    MANCHSETER_UNITED(33L, "Manchester United", 1L),
    LIVERPOOL(40L, "Liverpool", 2L),
    CHELSEA(49L, "Chelsea", 3L),
    ARSENAL(42L, "Arsenal", 4L),
    TOTTENHAM(47L, "Tottenham", 5L),
    MANCHESTER_CITY(50L, "Manchester City", 6L),

    ;

    private final Long externalId;
    private final String teamName;
    private final Long internalId;

    public static Long toInternalId(Long externalId) {
        for (TeamIdMap map : values()) {
            if (map.getExternalId() == externalId) {
                return map.getInternalId();
            }
        }
        throw new IllegalArgumentException("Invalid external team ID: " + externalId);
    }

    public static Long toExternalId(Long internalId) {
        for (TeamIdMap map : values()) {
            if (map.getInternalId() == internalId) {
                return map.getExternalId();
            }
        }
        throw new IllegalArgumentException("Invalid internal team ID: " + internalId);
    }
}
