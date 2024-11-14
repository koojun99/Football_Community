package honajun.football_community.league.service;

import honajun.football_community.league.entity.League;
import honajun.football_community.league.exception.LeagueException;
import honajun.football_community.league.exception.LeagueExceptionCode;
import lombok.RequiredArgsConstructor;
import honajun.football_community.global.annotation.Adapter;
import honajun.football_community.league.repository.LeagueRepository;

@Adapter
@RequiredArgsConstructor
public class LeagueQueryAdapter {

    private final LeagueRepository LeagueRepository;

    public League findById(Long leagueId) {
        return LeagueRepository.findById(leagueId)
                .orElseThrow(() -> new LeagueException(LeagueExceptionCode.LEAGUE_NOT_FOUND));
    }
}