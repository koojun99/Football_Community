package honajun.football_community.league.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import honajun.football_community.league.service.LeagueService;

@RestController
@RequiredArgsConstructor
public class LeagueController {

    private final LeagueService LeagueService;
}
