package honajun.football_community.league.controller;

import honajun.football_community.global.response.CommonResponse;
import honajun.football_community.league.dto.LeagueResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import honajun.football_community.league.service.LeagueService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/wiki/league")
public class LeagueController {

    private final LeagueService LeagueService;

    @Operation(summary = "리그 위키 조회", description = "리그 위키 페이지를 조회합니다.")
    @GetMapping("/{leagueId}")
    public CommonResponse<LeagueResponseDTO.getLeagueWiki> getLeagueWiki(
            @PathVariable Long leagueId
    ) {
        return CommonResponse.onSuccess(LeagueService.getLeagueWiki(leagueId));
    }
}
