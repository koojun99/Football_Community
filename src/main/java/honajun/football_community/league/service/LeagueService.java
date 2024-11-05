package honajun.football_community.league.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LeagueService {

    private final LeagueCommandAdapter LeagueCommandAdapter;
    private final LeagueQueryAdapter LeagueQueryAdapter;
}