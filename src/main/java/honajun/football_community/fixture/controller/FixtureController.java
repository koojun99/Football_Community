package honajun.football_community.fixture.controller;

import honajun.football_community.fixture.dto.FixtureResponseDTO;
import honajun.football_community.global.response.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import honajun.football_community.fixture.service.FixtureService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/fixtures")
public class FixtureController {

    private final FixtureService fixtureService;

    @Operation(summary = "팀의 경기 일정 조회", description = "팀의 경기 일정을 조회합니다.")
    @GetMapping("/team/{teamId}")
    public CommonResponse<FixtureResponseDTO.getFixtures> getTeamFixture(
            @PathVariable Long teamId) throws IOException {
        return CommonResponse.onSuccess(fixtureService.getTeamFixture(teamId));
    }

    @Operation(summary = "리그의 경기 일정 조회", description = "리그의 경기 일정을 조회합니다.")
    @GetMapping("/league/{leagueId}")
    public CommonResponse<FixtureResponseDTO.getFixtures> getLeagueFixture(
            @PathVariable Long leagueId) throws IOException {
        return CommonResponse.onSuccess(fixtureService.getLeagueFixture(leagueId));
    }

    @Operation(summary = "경기 상세조회", description = "경기 상세정보를 조회합니다.")
    @GetMapping("/{fixtureId}")
    public CommonResponse<FixtureResponseDTO.getFixture> getFixture(
            @PathVariable Long fixtureId) throws IOException {
        return CommonResponse.onSuccess(fixtureService.getFixture(fixtureId));
    }
}
