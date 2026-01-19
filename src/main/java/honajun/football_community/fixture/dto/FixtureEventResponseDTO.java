package honajun.football_community.fixture.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class FixtureEventResponseDTO {
    private String type; // 이벤트 타입 (예: "Goal", "Card", "subst" 등)
    private Integer time; // 이벤트 발생 시간 (분)
    private String detail; // 이벤트 상세 (예: "Normal Goal", "Yellow Card" 등)
    private String comments; // 이벤트 코멘트 (선택)
    private Long teamId; // 팀 ID
    private String teamName; // 팀 이름
    private Long playerId; // 선수 ID (선택)
    private String playerName; // 선수 이름 (선택)
    private Long assistId; // 어시스트 선수 ID (선택)
    private String assistName; // 어시스트 선수 이름 (선택)
}
