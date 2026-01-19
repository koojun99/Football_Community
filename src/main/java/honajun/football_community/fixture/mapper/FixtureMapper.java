package honajun.football_community.fixture.mapper;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import honajun.football_community.fixture.dto.FixtureEventResponseDTO;
import honajun.football_community.fixture.dto.FixtureLineupResponseDTO;
import honajun.football_community.fixture.dto.FixturePlayerStatsResponseDTO;
import honajun.football_community.fixture.dto.FixtureResponseDTO;
import honajun.football_community.fixture.dto.FixtureResponseDTO.getFixture;
import honajun.football_community.global.enums.fixture.FixtureStatus;
import honajun.football_community.global.http.FixtureApiResponse;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class FixtureMapper {

    private static final ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    public static FixtureResponseDTO.getFixtures toGetFixtures(String jsonResponse) throws IOException {
        // JSON 데이터를 ApiResponse 객체로 변환
        FixtureApiResponse apiResponse = objectMapper.readValue(jsonResponse, FixtureApiResponse.class);

        // ApiResponse의 데이터를 FixturesResponseDTO.getFixtures로 변환
        List<getFixture> fixtureList = apiResponse.getResponse().stream()
                .map(responseData -> FixtureResponseDTO.getFixture.builder()
                        .fixtureId(responseData.getFixture().getId())
                        .league(responseData.getLeague().getName())
                        .homeTeam(responseData.getTeams().getHome().getName())
                        .awayTeam(responseData.getTeams().getAway().getName())
                        .scores(FixtureResponseDTO.getScores.builder()
                                .homeTeamScore(responseData.getGoals().getHome())
                                .awayTeamScore(responseData.getGoals().getAway())
                                .build())
                        .fixtureDate(LocalDateTime.ofInstant(Instant.ofEpochSecond(responseData.getFixture().getTimestamp()), ZoneId.of("Asia/Seoul")))
                        .fixtureStatus(FixtureStatus.fromCode(responseData.getFixture().getStatus().getShortStatus()))
                        .build())
                .toList();

        return FixtureResponseDTO.getFixtures.builder()
                .fixtures(fixtureList)
                .build();
    }

    public static FixtureResponseDTO.getFixture toGetFixture(String jsonResponse) throws IOException {
        // JSON 데이터를 ApiResponse 객체로 변환
        FixtureApiResponse apiResponse = objectMapper.readValue(jsonResponse, FixtureApiResponse.class);

        // 응답이 비어있는 경우 예외 발생 (존재하지 않는 fixtureId 등)
        if (apiResponse.getResponse() == null || apiResponse.getResponse().isEmpty()) {
            throw new IOException("Fixture not found: API returned empty response");
        }

        return FixtureResponseDTO.getFixture.builder()
                .fixtureId(apiResponse.getResponse().get(0).getFixture().getId())
                .league(apiResponse.getResponse().get(0).getLeague().getName())
                .homeTeam(apiResponse.getResponse().get(0).getTeams().getHome().getName())
                .awayTeam(apiResponse.getResponse().get(0).getTeams().getAway().getName())
                .scores(FixtureResponseDTO.getScores.builder()
                        .homeTeamScore(apiResponse.getResponse().get(0).getGoals().getHome())
                        .awayTeamScore(apiResponse.getResponse().get(0).getGoals().getAway())
                        .build())
                .fixtureDate(LocalDateTime.ofInstant(Instant.ofEpochSecond(apiResponse.getResponse().get(0).getFixture().getTimestamp()), ZoneId.of("Asia/Seoul")))
                .fixtureStatus(FixtureStatus.fromCode(apiResponse.getResponse().get(0).getFixture().getStatus().getShortStatus()))
                .build();
    }

    /**
     * 이벤트 JSON 응답을 이벤트 DTO 리스트로 변환합니다.
     */
    public static List<FixtureEventResponseDTO> toGetEvents(String eventsJson) throws IOException {
        JsonNode rootNode = objectMapper.readTree(eventsJson);
        JsonNode responseNode = rootNode.get("response");

        if (responseNode == null || !responseNode.isArray()) {
            return new ArrayList<>();
        }

        List<FixtureEventResponseDTO> events = new ArrayList<>();
        for (JsonNode eventNode : responseNode) {
            JsonNode timeNode = eventNode.has("time") ? eventNode.get("time") : null;
            JsonNode teamNode = eventNode.has("team") ? eventNode.get("team") : null;
            JsonNode playerNode = eventNode.has("player") ? eventNode.get("player") : null;
            JsonNode assistNode = eventNode.has("assist") ? eventNode.get("assist") : null;

            FixtureEventResponseDTO event = FixtureEventResponseDTO.builder()
                    .type(eventNode.has("type") ? eventNode.get("type").asText() : null)
                    .time(timeNode != null && timeNode.has("elapsed") && !timeNode.get("elapsed").isNull() 
                            ? timeNode.get("elapsed").asInt() : null)
                    .detail(eventNode.has("detail") ? eventNode.get("detail").asText() : null)
                    .comments(eventNode.has("comments") ? eventNode.get("comments").asText() : null)
                    .teamId(teamNode != null && teamNode.has("id") && !teamNode.get("id").isNull() 
                            ? teamNode.get("id").asLong() : null)
                    .teamName(teamNode != null && teamNode.has("name") ? teamNode.get("name").asText() : null)
                    .playerId(playerNode != null && playerNode.has("id") && !playerNode.get("id").isNull() 
                            ? playerNode.get("id").asLong() : null)
                    .playerName(playerNode != null && playerNode.has("name") ? playerNode.get("name").asText() : null)
                    .assistId(assistNode != null && assistNode.has("id") && !assistNode.get("id").isNull() 
                            ? assistNode.get("id").asLong() : null)
                    .assistName(assistNode != null && assistNode.has("name") ? assistNode.get("name").asText() : null)
                    .build();
            events.add(event);
        }

        return events;
    }

    /**
     * 라인업 JSON 응답을 라인업 DTO로 변환합니다.
     *
     * 경기 시작 전이면 API-Football에서 response 배열이 비어있으므로
     * 이 경우 빈 라인업 리스트를 반환합니다.
     */
    public static FixtureLineupResponseDTO toGetLineups(String lineupsJson) throws IOException {
        JsonNode rootNode = objectMapper.readTree(lineupsJson);
        JsonNode responseNode = rootNode.get("response");

        if (responseNode == null || !responseNode.isArray() || responseNode.isEmpty()) {
            return FixtureLineupResponseDTO.builder()
                    .lineups(List.of())
                    .build();
        }

        List<FixtureLineupResponseDTO.TeamLineup> teamLineups = new java.util.ArrayList<>();

        for (JsonNode lineupNode : responseNode) {
            JsonNode teamNode = lineupNode.get("team");
            JsonNode formationNode = lineupNode.get("formation");
            JsonNode startXiNode = lineupNode.get("startXI");

            Long teamId = teamNode != null && teamNode.has("id") && !teamNode.get("id").isNull()
                    ? teamNode.get("id").asLong()
                    : null;
            String teamName = teamNode != null && teamNode.has("name")
                    ? teamNode.get("name").asText()
                    : null;
            String formation = formationNode != null && !formationNode.isNull()
                    ? formationNode.asText()
                    : null;

            List<FixtureLineupResponseDTO.Player> starters = new java.util.ArrayList<>();

            if (startXiNode != null && startXiNode.isArray()) {
                for (JsonNode playerWrapper : startXiNode) {
                    JsonNode playerNode = playerWrapper.get("player");
                    if (playerNode == null) {
                        continue;
                    }

                    FixtureLineupResponseDTO.Player player = FixtureLineupResponseDTO.Player.builder()
                            .playerId(playerNode.has("id") && !playerNode.get("id").isNull()
                                    ? playerNode.get("id").asLong()
                                    : null)
                            .playerName(playerNode.has("name") ? playerNode.get("name").asText() : null)
                            .number(playerNode.has("number") && !playerNode.get("number").isNull()
                                    ? playerNode.get("number").asInt()
                                    : null)
                            .position(playerNode.has("pos") ? playerNode.get("pos").asText() : null)
                            .grid(playerNode.has("grid") ? playerNode.get("grid").asText() : null)
                            .build();
                    starters.add(player);
                }
            }

            FixtureLineupResponseDTO.TeamLineup teamLineup = FixtureLineupResponseDTO.TeamLineup.builder()
                    .teamId(teamId)
                    .teamName(teamName)
                    .formation(formation)
                    .starters(starters)
                    .build();

            teamLineups.add(teamLineup);
        }

        return FixtureLineupResponseDTO.builder()
                .lineups(teamLineups)
                .build();
    }

    /**
     * 선수 통계 JSON 응답을 선수 스탯 DTO로 변환합니다.
     *
     * API-Football /players?fixture={fixtureId} 응답을 기반으로 합니다.
     */
    public static FixturePlayerStatsResponseDTO toGetPlayerStats(String playerStatsJson) throws IOException {
        JsonNode rootNode = objectMapper.readTree(playerStatsJson);
        JsonNode responseNode = rootNode.get("response");

        if (responseNode == null || !responseNode.isArray() || responseNode.isEmpty()) {
            // 선수 통계가 없는 경우에도 fixtureId만 null로 두고 빈 리스트 반환
            return FixturePlayerStatsResponseDTO.builder()
                    .fixtureId(null)
                    .players(List.of())
                    .build();
        }

        List<FixturePlayerStatsResponseDTO.PlayerStats> players = new ArrayList<>();
        Long fixtureId = null;

        for (JsonNode playerWrapper : responseNode) {
            JsonNode playerNode = playerWrapper.get("player");
            JsonNode statisticsArray = playerWrapper.get("statistics");

            if (statisticsArray == null || !statisticsArray.isArray() || statisticsArray.isEmpty()) {
                continue;
            }

            JsonNode statsNode = statisticsArray.get(0);

            JsonNode teamNode = statsNode.get("team");
            JsonNode gamesNode = statsNode.get("games");
            JsonNode shotsNode = statsNode.get("shots");
            JsonNode goalsNode = statsNode.get("goals");
            JsonNode passesNode = statsNode.get("passes");
            JsonNode tacklesNode = statsNode.get("tackles");
            JsonNode foulsNode = statsNode.get("fouls");
            JsonNode cardsNode = statsNode.get("cards");

            // fixtureId는 games.fixture_id 또는 별도 위치에 있을 수 있으나,
            // 안전하게 root-level parameters.fixture를 우선 시도합니다.
            if (fixtureId == null) {
                JsonNode parametersNode = rootNode.get("parameters");
                if (parametersNode != null && parametersNode.has("fixture")) {
                    try {
                        fixtureId = parametersNode.get("fixture").asLong();
                    } catch (Exception ignored) {
                    }
                }
            }

            FixturePlayerStatsResponseDTO.PlayerStats playerStats =
                    FixturePlayerStatsResponseDTO.PlayerStats.builder()
                            .playerId(playerNode != null && playerNode.has("id") && !playerNode.get("id").isNull()
                                    ? playerNode.get("id").asLong()
                                    : null)
                            .playerName(playerNode != null && playerNode.has("name")
                                    ? playerNode.get("name").asText()
                                    : null)
                            .teamId(teamNode != null && teamNode.has("id") && !teamNode.get("id").isNull()
                                    ? teamNode.get("id").asLong()
                                    : null)
                            .teamName(teamNode != null && teamNode.has("name")
                                    ? teamNode.get("name").asText()
                                    : null)
                            .position(gamesNode != null && gamesNode.has("position")
                                    ? gamesNode.get("position").asText()
                                    : null)
                            .minutes(gamesNode != null && gamesNode.has("minutes") && !gamesNode.get("minutes").isNull()
                                    ? gamesNode.get("minutes").asInt()
                                    : null)
                            .goals(goalsNode != null && goalsNode.has("total") && !goalsNode.get("total").isNull()
                                    ? goalsNode.get("total").asInt()
                                    : null)
                            .assists(goalsNode != null && goalsNode.has("assists") && !goalsNode.get("assists").isNull()
                                    ? goalsNode.get("assists").asInt()
                                    : null)
                            .shotsTotal(shotsNode != null && shotsNode.has("total") && !shotsNode.get("total").isNull()
                                    ? shotsNode.get("total").asInt()
                                    : null)
                            .shotsOnTarget(shotsNode != null && shotsNode.has("on") && !shotsNode.get("on").isNull()
                                    ? shotsNode.get("on").asInt()
                                    : null)
                            .passesTotal(passesNode != null && passesNode.has("total") && !passesNode.get("total").isNull()
                                    ? passesNode.get("total").asInt()
                                    : null)
                            .passesKey(passesNode != null && passesNode.has("key") && !passesNode.get("key").isNull()
                                    ? passesNode.get("key").asInt()
                                    : null)
                            .passesAccuracy(passesNode != null && passesNode.has("accuracy") && !passesNode.get("accuracy").isNull()
                                    ? passesNode.get("accuracy").asText()
                                    : null)
                            .tacklesTotal(tacklesNode != null && tacklesNode.has("total") && !tacklesNode.get("total").isNull()
                                    ? tacklesNode.get("total").asInt()
                                    : null)
                            .interceptions(tacklesNode != null && tacklesNode.has("interceptions") && !tacklesNode.get("interceptions").isNull()
                                    ? tacklesNode.get("interceptions").asInt()
                                    : null)
                            .foulsCommitted(foulsNode != null && foulsNode.has("committed") && !foulsNode.get("committed").isNull()
                                    ? foulsNode.get("committed").asInt()
                                    : null)
                            .foulsDrawn(foulsNode != null && foulsNode.has("drawn") && !foulsNode.get("drawn").isNull()
                                    ? foulsNode.get("drawn").asInt()
                                    : null)
                            .yellowCards(cardsNode != null && cardsNode.has("yellow") && !cardsNode.get("yellow").isNull()
                                    ? cardsNode.get("yellow").asInt()
                                    : null)
                            .redCards(cardsNode != null && cardsNode.has("red") && !cardsNode.get("red").isNull()
                                    ? cardsNode.get("red").asInt()
                                    : null)
                            .build();

            players.add(playerStats);
        }

        return FixturePlayerStatsResponseDTO.builder()
                .fixtureId(fixtureId)
                .players(players)
                .build();
    }
}
