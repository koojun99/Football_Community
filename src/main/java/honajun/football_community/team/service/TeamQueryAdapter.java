package honajun.football_community.team.service;

import honajun.football_community.team.entity.Team;
import honajun.football_community.team.exception.TeamException;
import honajun.football_community.team.exception.TeamExceptionCode;
import lombok.RequiredArgsConstructor;
import honajun.football_community.global.annotation.Adapter;
import honajun.football_community.team.repository.TeamRepository;

@Adapter
@RequiredArgsConstructor
public class TeamQueryAdapter {

    private final TeamRepository TeamRepository;

    public Team findById(Long teamId) {
        return TeamRepository.findById(teamId)
                .orElseThrow(() -> new TeamException(TeamExceptionCode.TEAM_NOT_FOUND));
    }
}