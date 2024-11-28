package honajun.football_community.team.service;

import honajun.football_community.team.entity.Team;
import honajun.football_community.team.exception.TeamException;
import honajun.football_community.team.exception.TeamExceptionCode;
import lombok.RequiredArgsConstructor;
import honajun.football_community.global.annotation.Adapter;
import honajun.football_community.team.repository.TeamRepository;
import org.springframework.cache.annotation.Cacheable;

@Adapter
@RequiredArgsConstructor
public class TeamQueryAdapter {

    private final TeamRepository TeamRepository;

    @Cacheable(cacheNames = "teams", key = "#teamId")
    public Team findById(Long teamId) {
        return TeamRepository.findById(teamId)
                .orElseThrow(() -> new TeamException(TeamExceptionCode.TEAM_NOT_FOUND));
    }
}