package honajun.football_community.global.http;

import java.io.IOException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 외부 API를 통해 경기 정보를 조회하는 클라이언트
 */
@Component
public class FixtureApiClient {

    @Value("${football-api.base-url}")
    private String BASE_URL;

    @Value("${football-api.key}")
    private String API_KEY;

    private final OkHttpClient client;

    public FixtureApiClient() {
        this.client = new OkHttpClient();
    }

    /**
     * 특정 팀의 경기 일정을 외부 API에서 조회합니다.
     */
    public String getTeamFixtures(Long teamId, String season, int next, String timezone) throws IOException {
        // URL 구성
        String url = String.format(
                "%s/fixtures?season=%s&team=%d&next=%d&timezone=%s",
                BASE_URL, season, teamId, next, timezone);

        // 요청 생성
        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("x-apisports-key", API_KEY)
                .build();

        // 요청 실행 및 응답 반환
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                return response.body().string();
            } else {
                throw new IOException("Failed to fetch fixtures. HTTP Code: " + response.code());
            }
        }
    }

    /**
     * 특정 리그의 경기 일정을 외부 API에서 조회합니다.
     */
    public String getLeagueFixtures(Long leagueId, String season, int next, String timezone) throws IOException {
        // URL 구성
        String url = String.format(
                "%s/fixtures?season=%s&league=%d&next=%d&timezone=%s",
                BASE_URL, season, leagueId, next, timezone);

        // 요청 생성
        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("x-apisports-key", API_KEY)
                .build();

        // 요청 실행 및 응답 반환
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                return response.body().string();
            } else {
                throw new IOException("Failed to fetch fixtures. HTTP Code: " + response.code());
            }
        }
    }

    /**
     * 특정 경기의 상세 정보를 외부 API에서 조회합니다.
     */
    public String getFixture(Long fixtureId, String timezone) throws IOException {
        // URL 구성
        String url = String.format(
                "%s/fixtures?id=%d&timezone=%s",
                BASE_URL, fixtureId, timezone);

        // 요청 생성
        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("x-apisports-key", API_KEY)
                .build();

        // 요청 실행 및 응답 반환
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                return response.body().string();
            } else {
                throw new IOException("Failed to fetch fixtures. HTTP Code: " + response.code());
            }
        }
    }

    /**
     * 오늘을 기준으로 가장 가까운 날짜에 예정된 경기들을 조회합니다.
     * 
     * @param leagueId 리그 ID (기본값: 39 - 프리미어리그)
     * @param season   시즌 (예: "2024")
     * @param next     조회할 다음 경기 수 (기본값: 10)
     * @param timezone 타임존 (기본값: "Asia/Seoul")
     * @return JSON 형식의 경기 정보
     */
    public String getUpcomingFixtures(Long leagueId, String season, int next, String timezone) throws IOException {
        // URL 구성
        String url = String.format(
                "%s/fixtures?league=%d&season=%s&next=%d&timezone=%s",
                BASE_URL, leagueId, season, next, timezone);

        // 요청 생성
        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("x-apisports-key", API_KEY)
                .build();

        // 요청 실행 및 응답 반환
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                return response.body().string();
            } else {
                throw new IOException("Failed to fetch upcoming fixtures. HTTP Code: " + response.code());
            }
        }
    }

    /**
     * 오늘을 기준으로 이미 끝난 경기 기록들을 조회합니다.
     * 
     * @param leagueId 리그 ID (기본값: 39 - 프리미어리그)
     * @param season   시즌 (예: "2024")
     * @param last     조회할 과거 경기 수 (기본값: 10)
     * @param timezone 타임존 (기본값: "Asia/Seoul")
     * @return JSON 형식의 경기 정보
     */
    public String getPastFixtures(Long leagueId, String season, int last, String timezone) throws IOException {
        // URL 구성
        String url = String.format(
                "%s/fixtures?league=%d&season=%s&last=%d&timezone=%s",
                BASE_URL, leagueId, season, last, timezone);

        // 요청 생성
        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("x-apisports-key", API_KEY)
                .build();

        // 요청 실행 및 응답 반환
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                return response.body().string();
            } else {
                throw new IOException("Failed to fetch past fixtures. HTTP Code: " + response.code());
            }
        }
    }

    /**
     * 특정 경기의 실시간 이벤트 정보를 외부 API에서 조회합니다.
     * 
     * @param fixtureId 경기 ID
     * @param teamId    (선택) 특정 팀 ID로 필터링
     * @param playerId  (선택) 특정 선수 ID로 필터링
     * @param eventType (선택) 이벤트 타입으로 필터링 (예: "card", "goal")
     * @return JSON 형식의 이벤트 정보
     */
    public String getFixtureEvents(Long fixtureId, Long teamId, Long playerId, String eventType) throws IOException {
        // URL 구성
        StringBuilder urlBuilder = new StringBuilder(
                String.format("%s/fixtures/events?fixture=%d", BASE_URL, fixtureId));

        if (teamId != null) {
            urlBuilder.append("&team=").append(teamId);
        }
        if (playerId != null) {
            urlBuilder.append("&player=").append(playerId);
        }
        if (eventType != null && !eventType.isEmpty()) {
            urlBuilder.append("&type=").append(eventType);
        }

        // 요청 생성
        Request request = new Request.Builder()
                .url(urlBuilder.toString())
                .get()
                .addHeader("x-apisports-key", API_KEY)
                .build();

        // 요청 실행 및 응답 반환
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                return response.body().string();
            } else {
                throw new IOException("Failed to fetch fixture events. HTTP Code: " + response.code());
            }
        }
    }

    /**
     * 특정 경기의 선발 라인업 정보를 외부 API에서 조회합니다.
     *
     * API-Football: GET /fixtures/lineups?fixture={fixtureId}
     *
     * 경기 시작 전이면 response 배열이 비어있으므로,
     * 서비스/매퍼 단에서 이를 감지해 빈 라인업을 반환합니다.
     */
    public String getFixtureLineups(Long fixtureId) throws IOException {
        String url = String.format("%s/fixtures/lineups?fixture=%d", BASE_URL, fixtureId);

        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("x-apisports-key", API_KEY)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                return response.body().string();
            } else {
                throw new IOException("Failed to fetch fixture lineups. HTTP Code: " + response.code());
            }
        }
    }

    /**
     * 특정 경기의 선수별 통계를 외부 API에서 조회합니다.
     *
     * API-Football: GET /players?fixture={fixtureId}
     * (team 파라미터는 선택이며, 여기서는 전체 선수 통계를 조회합니다.)
     */
    public String getFixturePlayerStats(Long fixtureId) throws IOException {
        String url = String.format("%s/players?fixture=%d", BASE_URL, fixtureId);

        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("x-apisports-key", API_KEY)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                return response.body().string();
            } else {
                throw new IOException("Failed to fetch fixture player stats. HTTP Code: " + response.code());
            }
        }
    }

    /**
     * 특정 리그의 이적 정보를 외부 API에서 조회합니다.
     *
     * API-Football: GET /transfers?league={leagueId}&season={season}
     */
    public String getTransfersByLeague(Long leagueId, String season) throws IOException {
        String url = String.format("%s/transfers?league=%d&season=%s", BASE_URL, leagueId, season);

        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("x-apisports-key", API_KEY)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                return response.body().string();
            } else {
                throw new IOException("Failed to fetch transfers. HTTP Code: " + response.code());
            }
        }
    }
}
