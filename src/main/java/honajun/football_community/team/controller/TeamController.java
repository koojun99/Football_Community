package honajun.football_community.team.controller;

import honajun.football_community.global.response.CommonResponse;
import honajun.football_community.team.dto.TeamResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import honajun.football_community.team.service.TeamService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/wiki/team")
public class TeamController {
    
    private final TeamService teamService;

    @Operation(summary = "팀 위키 조회", description = "팀 위키 페이지를 조회합니다.")
    @GetMapping("/{teamId}")
    public CommonResponse<TeamResponseDTO.getTeamWiki> getTeamWiki(
            @PathVariable Long teamId
    ) {
        return CommonResponse.onSuccess(teamService.getTeamWiki(teamId));
    }

}
