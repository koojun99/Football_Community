package honajun.football_community.fixture.service;

import honajun.football_community.fixture.dto.FixtureResponseDTO;
import honajun.football_community.fixture.mapper.FixtureMapper;
import honajun.football_community.global.enums.fixture.LeagueIdMap;
import honajun.football_community.global.enums.fixture.TeamIdMap;
import honajun.football_community.global.http.FixtureApiClient;
import honajun.football_community.league.service.LeagueQueryAdapter;
import honajun.football_community.team.service.TeamQueryAdapter;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FixtureService {

    private final LeagueQueryAdapter leagueQueryAdapter;
    private final TeamQueryAdapter teamQueryAdapter;
    private final FixtureApiClient fixtureApiClient;

    public FixtureResponseDTO.getFixtures getTeamFixture(Long teamId) throws IOException {
        Long externalTeamId = TeamIdMap.toExternalId(teamId);
        String apiResult = fixtureApiClient.getTeamFixtures(externalTeamId, "2024",10,   "Asia/Seoul");
        return FixtureMapper.toGetFixtures(apiResult);
    }

    public FixtureResponseDTO.getFixtures getLeagueFixture(Long leagueId) throws IOException {
        Long externalLeagueId = LeagueIdMap.toExternalId(leagueId);
        String apiResult = fixtureApiClient.getLeagueFixtures(externalLeagueId, "2024", 10, "Asia/Seoul");
        return FixtureMapper.toGetFixtures(apiResult);
    }

    public FixtureResponseDTO.getFixture getFixture(Long fixtureId) throws IOException {
        String apiResult = fixtureApiClient.getFixture(fixtureId, "Asia/Seoul");
        return FixtureMapper.toGetFixture(apiResult);
    }
}