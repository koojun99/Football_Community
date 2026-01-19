package honajun.football_community.fixture.controller;

import honajun.football_community.fixture.dto.FixtureLineupResponseDTO;
import honajun.football_community.fixture.dto.FixturePlayerStatsResponseDTO;
import honajun.football_community.fixture.dto.FixtureResponseDTO;
import honajun.football_community.fixture.service.FixtureService;
import honajun.football_community.global.annotation.AuthMember;
import honajun.football_community.global.response.CommonResponse;
import honajun.football_community.member.entity.Member;
import io.swagger.v3.oas.annotations.Operation;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 경기 정보를 조회하는 컨트롤러
 */
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

    @Operation(summary = "오늘 기준 예정된 경기 조회", description = "오늘을 기준으로 가장 가까운 날짜에 예정된 경기들을 조회합니다. 기본값: 프리미어리그(39), Asia/Seoul 타임존")
    @GetMapping("/upcoming")
    public CommonResponse<FixtureResponseDTO.getFixtures> getUpcomingFixtures(
            @RequestParam(required = false) Long leagueId,
            @RequestParam(required = false) String timezone) throws IOException {
        return CommonResponse.onSuccess(fixtureService.getUpcomingFixtures(leagueId, timezone));
    }

    @Operation(summary = "오늘 기준 과거 경기 기록 조회", description = "오늘을 기준으로 이미 끝난 경기 기록들을 조회합니다. 기본값: 프리미어리그(39), Asia/Seoul 타임존, 최근 10경기")
    @GetMapping("/past")
    public CommonResponse<FixtureResponseDTO.getFixtures> getPastFixtures(
            @RequestParam(required = false) Long leagueId,
            @RequestParam(required = false) String timezone,
            @RequestParam(required = false) Integer last) throws IOException {
        return CommonResponse.onSuccess(fixtureService.getPastFixtures(leagueId, timezone, last));
    }

    @Operation(summary = "Follow한 팀/경기의 경기 정보 조회", description = "사용자가 Follow한 팀의 모든 경기와 Follow한 경기 정보를 조회합니다. 스케줄러가 백그라운드에서 주기적으로 업데이트한 최신 데이터를 반환합니다.")
    @GetMapping("/followed")
    public CommonResponse<FixtureResponseDTO.getFixtures> getFollowedFixtures(
            @AuthMember Member member) throws IOException {
        return CommonResponse.onSuccess(fixtureService.getFollowedFixtures(member));
    }

    @Operation(summary = "경기 상세조회", description = "경기 상세정보를 조회합니다.")
    @GetMapping("/{fixtureId}")
    public CommonResponse<FixtureResponseDTO.getFixture> getFixture(
            @PathVariable Long fixtureId) throws IOException {
        return CommonResponse.onSuccess(fixtureService.getFixture(fixtureId));
    }

    @Operation(summary = "경기 선발 라인업 조회",
            description = "특정 경기의 선발 라인업을 조회합니다. 경기 시작 전이면 빈 배열을 반환합니다.")
    @GetMapping("/{fixtureId}/lineups")
    public CommonResponse<FixtureLineupResponseDTO> getFixtureLineups(
            @PathVariable Long fixtureId) throws IOException {
        return CommonResponse.onSuccess(fixtureService.getFixtureLineups(fixtureId));
    }

    @Operation(summary = "경기 선수 통계 조회",
            description = "특정 경기에서 선수별 주요 스탯을 조회합니다. 경기 시작 전이거나 데이터가 없으면 빈 배열을 반환합니다.")
    @GetMapping("/{fixtureId}/players")
    public CommonResponse<FixturePlayerStatsResponseDTO> getFixturePlayerStats(
            @PathVariable Long fixtureId) throws IOException {
        return CommonResponse.onSuccess(fixtureService.getFixturePlayerStats(fixtureId));
    }
}
