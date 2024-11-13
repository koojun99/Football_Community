package honajun.football_community.league.service;

import honajun.football_community.league.dto.LeagueResponseDTO;
import honajun.football_community.league.entity.League;
import honajun.football_community.league.mapper.LeagueMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LeagueService {

    private final LeagueCommandAdapter leagueCommandAdapter;
    private final LeagueQueryAdapter leagueQueryAdapter;

}