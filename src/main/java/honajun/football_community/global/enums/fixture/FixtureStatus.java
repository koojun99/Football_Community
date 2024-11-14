package honajun.football_community.global.enums.fixture;

import honajun.football_community.fixture.exception.FixtureException;
import honajun.football_community.fixture.exception.FixtureExceptionCode;
import lombok.Getter;

@Getter
public enum FixtureStatus {

    TBD("Time To Be Defined", "Scheduled"),
    NS("Not Started", "Scheduled"),
    H1("First Half, Kick Off", "In Play"),
    HT("Halftime", "In Play"),
    H2("Second Half, 2nd Half Started", "In Play"),
    ET("Extra Time", "In Play"),
    BT("Break Time", "In Play"),
    P("Penalty In Progress", "In Play"),
    SUSP("Match Suspended", "In Play"),
    INT("Match Interrupted", "In Play"),
    FT("Match Finished", "Finished"),
    AET("Match Finished After Extra Time", "Finished"),
    PEN("Match Finished After Penalty Shootout", "Finished"),
    PST("Match Postponed", "Postponed"),
    CANC("Match Cancelled", "Cancelled"),
    ABD("Match Abandoned", "Abandoned"),
    AWD("Technical Loss", "Not Played"),
    WO("WalkOver", "Not Played"),
    LIVE("In Progress", "In Play");

    private final String description; // 상태에 대한 설명
    private final String category;    // 상태의 카테고리

    FixtureStatus(String description, String category) {
        this.description = description;
        this.category = category;
    }

    /**
     * 상태 코드로 FixtureStatus를 찾는 유틸리티 메서드
     */
    public static FixtureStatus fromCode(String code) {
        for (FixtureStatus status : values()) {
            if (status.name().equalsIgnoreCase(code)) {
                return status;
            }
        }
        throw new FixtureException(FixtureExceptionCode.INVALID_STATUS);
    }
}
