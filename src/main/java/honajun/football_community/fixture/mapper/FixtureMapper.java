package honajun.football_community.fixture.mapper;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import honajun.football_community.fixture.dto.FixtureResponseDTO;
import honajun.football_community.fixture.dto.FixtureResponseDTO.getFixture;
import honajun.football_community.global.enums.fixture.FixtureStatus;
import honajun.football_community.global.http.FixtureApiResponse;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
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
}
