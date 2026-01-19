package honajun.football_community.fixture.service;

import honajun.football_community.fixture.dto.TransferResponseDTO;
import honajun.football_community.fixture.mapper.TransferMapper;
import honajun.football_community.global.http.FixtureApiClient;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 이적시장(Transfers) 정보를 조회하는 서비스
 */
@Service
@RequiredArgsConstructor
public class TransferService {

    private final FixtureApiClient fixtureApiClient;

    public enum TransferWindow {
        SUMMER,
        WINTER,
        OFF
    }

    /**
     * 오늘 날짜를 기준으로 현재 이적시장 윈도우(여름/겨울/없음)를 판별합니다.
     *
     * 단순화된 기준 (유럽 리그 기준):
     * - 겨울 이적시장: 1월 1일 ~ 1월 31일
     * - 여름 이적시장: 7월 1일 ~ 8월 31일
     * - 그 외: OFF
     */
    private TransferWindow getCurrentWindow(LocalDate today) {
        LocalDate winterStart = LocalDate.of(today.getYear(), Month.JANUARY, 1);
        LocalDate winterEnd = LocalDate.of(today.getYear(), Month.JANUARY, 31);

        LocalDate summerStart = LocalDate.of(today.getYear(), Month.JULY, 1);
        LocalDate summerEnd = LocalDate.of(today.getYear(), Month.AUGUST, 31);

        if (!today.isBefore(winterStart) && !today.isAfter(winterEnd)) {
            return TransferWindow.WINTER;
        }
        if (!today.isBefore(summerStart) && !today.isAfter(summerEnd)) {
            return TransferWindow.SUMMER;
        }
        return TransferWindow.OFF;
    }

    /**
     * 개별 이적 건이 주어진 윈도우(여름/겨울)에 포함되는지 여부를 판단합니다.
     *
     * 여기서는 "연도"가 아니라 **월/일 기준**으로만 판별합니다.
     * 실제 연도는 시즌 및 API-Football에서 반환하는 날짜에 의해 결정되므로,
     * 같은 시즌 안의 겨울/여름 이적만 필터링되도록 합니다.
     */
    private boolean isInWindow(LocalDate transferDate, TransferWindow window) {
        if (transferDate == null || window == TransferWindow.OFF) {
            return false;
        }

        if (window == TransferWindow.WINTER) {
            Month m = transferDate.getMonth();
            int day = transferDate.getDayOfMonth();
            return (m == Month.JANUARY) && (day >= 1 && day <= 31);
        }

        if (window == TransferWindow.SUMMER) {
            Month m = transferDate.getMonth();
            int day = transferDate.getDayOfMonth();
            return (m == Month.JULY && day >= 1) || (m == Month.AUGUST && day <= 31);
        }

        return false;
    }

    /**
     * 프리미어리그 시즌 계산 로직과 동일한 방식으로 시즌 문자열을 계산합니다.
     * 8월 이전이면 전년도 시즌을 사용합니다.
     */
    private String calculateSeason() {
        LocalDate now = LocalDate.now(ZoneId.of("Asia/Seoul"));
        int currentYear = now.getYear();
        int month = now.getMonthValue();
        return (month < 8) ? String.valueOf(currentYear - 1) : String.valueOf(currentYear);
    }

    /**
     * 오늘 날짜를 기준으로 현재 이적시장 윈도우(여름/겨울)에 해당하는 이적 정보만 조회합니다.
     *
     * - leagueId가 null이면 기본값 39 (프리미어리그)를 사용합니다.
     * - API-Football /transfers?league={leagueId}&season={season}를 호출합니다.
     * - 오늘 날짜 기준 겨울/여름 이적시장 기간에 해당하는 이적만 필터링합니다.
     * - 현재가 이적시장 기간이 아니면 window="OFF"와 함께 빈 transfers 리스트를 반환합니다.
     */
    @Transactional(readOnly = true)
    public TransferResponseDTO.getTransfers getCurrentWindowTransfers(Long leagueId) throws IOException {
        // 기본 리그: 프리미어리그
        if (leagueId == null) {
            leagueId = 39L;
        }

        LocalDate today = LocalDate.now(ZoneId.of("Asia/Seoul"));
        TransferWindow window = getCurrentWindow(today);

        String season = calculateSeason();
        String apiResult = fixtureApiClient.getTransfersByLeague(leagueId, season);
        TransferResponseDTO.getTransfers allTransfers = TransferMapper.toGetTransfers(apiResult);

        List<TransferResponseDTO.Transfer> filtered = allTransfers.getTransfers().stream()
                .filter(t -> isInWindow(t.getTransferDate(), window))
                .collect(Collectors.toList());

        return TransferResponseDTO.getTransfers.builder()
                .leagueId(leagueId)
                .season(season)
                .window(window.name())
                .transfers(window == TransferWindow.OFF ? List.of() : filtered)
                .build();
    }
}

