package honajun.football_community.fixture.service;

import honajun.football_community.fixture.dto.FixtureEventResponseDTO;
import honajun.football_community.fixture.dto.FixtureLineupResponseDTO;
import honajun.football_community.fixture.dto.FixturePlayerStatsResponseDTO;
import honajun.football_community.fixture.dto.FixtureResponseDTO;
import honajun.football_community.fixture.entity.Follow;
import honajun.football_community.fixture.mapper.FixtureMapper;
import honajun.football_community.global.http.FixtureApiClient;
import honajun.football_community.member.entity.Member;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 경기 정보를 조회하는 서비스
 */
@Service
@RequiredArgsConstructor
public class FixtureService {

    private final FixtureApiClient fixtureApiClient;
    private final FollowQueryAdapter followQueryAdapter;

    /**
     * 특정 팀의 경기 일정을 조회합니다.
     */
    @Cacheable(cacheNames = "teamFixtures", key = "#teamId")
    public FixtureResponseDTO.getFixtures getTeamFixture(Long teamId) throws IOException {
        // teamId는 API-Football에서 사용하는 ID를 직접 사용
        String apiResult = fixtureApiClient.getTeamFixtures(teamId, "2025", 10, "Asia/Seoul");
        return FixtureMapper.toGetFixtures(apiResult);
    }

    /**
     * 특정 리그의 경기 일정을 조회합니다.
     */
    @Cacheable(cacheNames = "leagueFixtures", key = "#leagueId")
    public FixtureResponseDTO.getFixtures getLeagueFixture(Long leagueId) throws IOException {
        // leagueId는 API-Football에서 사용하는 ID를 직접 사용
        String apiResult = fixtureApiClient.getLeagueFixtures(leagueId, "2024", 10, "Asia/Seoul");
        return FixtureMapper.toGetFixtures(apiResult);
    }

    /**
     * 특정 경기 상세 정보를 조회합니다.
     * 진행 중인 경기는 캐시를 사용하며, 종료된 경기는 events 필드가 포함되어야 하므로 캐시를 사용하지 않습니다.
     * 스케줄러가 백그라운드에서 주기적으로 업데이트하지만,
     * 사용자 요청 시에는 최신 데이터를 보장하기 위해 직접 조회합니다.
     * 종료된 경기의 경우 이벤트 기록도 함께 조회합니다.
     */
    @Cacheable(cacheNames = "fixtures", key = "#fixtureId", unless = "#result.fixtureStatus.isFinished()")
    public FixtureResponseDTO.getFixture getFixture(Long fixtureId) throws IOException {
        String apiResult = fixtureApiClient.getFixture(fixtureId, "Asia/Seoul");
        FixtureResponseDTO.getFixture fixture = FixtureMapper.toGetFixture(apiResult);

        // 종료된 경기의 경우 이벤트 기록도 조회
        if (fixture.getFixtureStatus().isFinished()) {
            try {
                String eventsJson = fixtureApiClient.getFixtureEvents(fixtureId, null, null, null);
                List<FixtureEventResponseDTO> events = FixtureMapper.toGetEvents(eventsJson);
                return FixtureResponseDTO.getFixture.builder()
                        .fixtureId(fixture.getFixtureId())
                        .league(fixture.getLeague())
                        .homeTeam(fixture.getHomeTeam())
                        .awayTeam(fixture.getAwayTeam())
                        .scores(fixture.getScores())
                        .fixtureDate(fixture.getFixtureDate())
                        .fixtureStatus(fixture.getFixtureStatus())
                        .events(events)
                        .build();
            } catch (IOException e) {
                // 이벤트 조회 실패 시 이벤트 없이 반환
                return fixture;
            }
        }

        return fixture;
    }

    /**
     * 특정 경기의 선발 라인업을 조회합니다.
     *
     * - API-Football /fixtures/lineups 엔드포인트를 호출합니다.
     * - 경기 시작 전이면 API가 빈 response 배열을 반환하므로, 이 경우 빈 lineups 리스트를 반환합니다.
     * - 경기 상태(과거/실시간)에 상관없이 동일한 방식으로 조회 가능합니다.
     */
    @Transactional(readOnly = true)
    public FixtureLineupResponseDTO getFixtureLineups(Long fixtureId) throws IOException {
        String lineupsJson = fixtureApiClient.getFixtureLineups(fixtureId);
        return FixtureMapper.toGetLineups(lineupsJson);
    }

    /**
     * 특정 경기의 선수별 통계를 조회합니다.
     *
     * - API-Football /players?fixture={fixtureId} 엔드포인트를 호출합니다.
     * - 과거 경기/진행 중 경기 모두 동일하게 조회 가능합니다.
     * - 경기 시작 전이거나 데이터가 없으면 빈 players 리스트가 반환될 수 있습니다.
     */
    @Transactional(readOnly = true)
    public FixturePlayerStatsResponseDTO getFixturePlayerStats(Long fixtureId) throws IOException {
        String playerStatsJson = fixtureApiClient.getFixturePlayerStats(fixtureId);
        return FixtureMapper.toGetPlayerStats(playerStatsJson);
    }

    /**
     * 시즌을 계산합니다.
     * 프리미어리그는 8월에 시작하므로, 8월 이전이면 전년도 시즌입니다.
     */
    private String calculateSeason() {
        java.time.LocalDate now = java.time.LocalDate.now();
        int currentYear = now.getYear();
        int month = now.getMonthValue();
        // 8월 이전이면 전년도 시즌 (예: 2025년 1월이면 2024 시즌)
        return (month < 8) ? String.valueOf(currentYear - 1) : String.valueOf(currentYear);
    }

    /**
     * 오늘을 기준으로 가장 가까운 날짜에 예정된 경기들을 조회합니다.
     * 기본값: 프리미어리그(39), Asia/Seoul 타임존
     * 
     * @param leagueId 리그 ID (기본값: 39 - 프리미어리그)
     * @param timezone 타임존 (기본값: "Asia/Seoul")
     * @return 예정된 경기 목록
     */
    @Cacheable(cacheNames = "upcomingFixtures", key = "#leagueId + '_' + #timezone")
    public FixtureResponseDTO.getFixtures getUpcomingFixtures(Long leagueId, String timezone) throws IOException {
        // 기본값 설정
        if (leagueId == null) {
            leagueId = 39L; // 프리미어리그
        }
        if (timezone == null || timezone.isEmpty()) {
            timezone = "Asia/Seoul";
        }

        String season = calculateSeason();
        String apiResult = fixtureApiClient.getUpcomingFixtures(leagueId, season, 10, timezone);
        return FixtureMapper.toGetFixtures(apiResult);
    }

    /**
     * 오늘을 기준으로 이미 끝난 경기 기록들을 조회합니다.
     * 기본값: 프리미어리그(39), Asia/Seoul 타임존
     * 
     * @param leagueId 리그 ID (기본값: 39 - 프리미어리그)
     * @param timezone 타임존 (기본값: "Asia/Seoul")
     * @param last     조회할 과거 경기 수 (기본값: 10)
     * @return 과거 경기 목록
     */
    @Cacheable(cacheNames = "pastFixtures", key = "#leagueId + '_' + #timezone + '_' + #last")
    public FixtureResponseDTO.getFixtures getPastFixtures(Long leagueId, String timezone, Integer last)
            throws IOException {
        // 기본값 설정
        if (leagueId == null) {
            leagueId = 39L; // 프리미어리그
        }
        if (timezone == null || timezone.isEmpty()) {
            timezone = "Asia/Seoul";
        }
        if (last == null || last <= 0) {
            last = 10; // 기본값: 최근 10경기
        }

        String season = calculateSeason();
        String apiResult = fixtureApiClient.getPastFixtures(leagueId, season, last, timezone);
        return FixtureMapper.toGetFixtures(apiResult);
    }

    /**
     * 사용자가 Follow한 경기의 경기 정보를 조회합니다.
     * 스케줄러가 백그라운드에서 주기적으로 업데이트한 캐시된 데이터를 반환합니다.
     * 외부 API 호출 없이 캐시에서 조회하여 비용을 절감합니다.
     */
    public FixtureResponseDTO.getFixtures getFollowedFixtures(Member member) throws IOException {
        List<FixtureResponseDTO.getFixture> allFixtures = new ArrayList<>();

        // Follow한 경기들의 정보 조회
        List<Follow> follows = followQueryAdapter.findAllByMember(member);
        Set<Long> uniqueFixtureIds = follows.stream()
                .map(Follow::getFixtureId)
                .collect(Collectors.toSet());

        for (Long fixtureId : uniqueFixtureIds) {
            try {
                // 캐시에서 조회 (스케줄러가 주기적으로 업데이트)
                FixtureResponseDTO.getFixture fixture = getFixture(fixtureId);
                allFixtures.add(fixture);
            } catch (IOException e) {
                // 개별 경기 조회 실패 시 스킵
                continue;
            }
        }

        return FixtureResponseDTO.getFixtures.builder()
                .fixtures(allFixtures)
                .build();
    }
}