package honajun.football_community.fixture.dto;

import java.time.LocalDate;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TransferResponseDTO {

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class Transfer {
        private Long playerId;
        private String playerName;
        private Long fromTeamId;
        private String fromTeamName;
        private Long toTeamId;
        private String toTeamName;
        private LocalDate transferDate;
        private String transferType; // 예: Transfer, Loan 등
        private String fee; // 문자열 형식의 이적료 (제공되는 경우)
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class getTransfers {
        private Long leagueId;
        private String season;
        /**
         * 현재 날짜 기준 이적시장 윈도우
         * - "SUMMER": 여름 이적시장
         * - "WINTER": 겨울 이적시장
         * - "OFF": 이적시장이 아닌 기간
         */
        private String window;
        private List<Transfer> transfers;
    }
}

