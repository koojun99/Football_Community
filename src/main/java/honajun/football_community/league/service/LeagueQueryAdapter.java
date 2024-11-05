package honajun.football_community.league.service;

import lombok.RequiredArgsConstructor;
import honajun.football_community.global.annotation.Adapter;
import honajun.football_community.league.repository.LeagueRepository;

@Adapter
@RequiredArgsConstructor
public class LeagueQueryAdapter {

    private final LeagueRepository LeagueRepository;
}