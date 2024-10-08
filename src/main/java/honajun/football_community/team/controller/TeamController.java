package honajun.football_community.team.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import honajun.football_community.team.service.TeamService;

@RestController
@RequiredArgsConstructor
public class TeamController {
    
    private final TeamService TeamService;
}
