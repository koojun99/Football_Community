package honajun.football_community.team.service;

import honajun.football_community.team.dto.TeamResponseDTO;
import honajun.football_community.team.entity.Team;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamCommandAdapter TeamCommandAdapter;
    private final TeamQueryAdapter TeamQueryAdapter;
    private final TeamQueryAdapter teamQueryAdapter;

//    @Transactional(readOnly = true)
//    public TeamResponseDTO.getTeamWiki getTeamWiki(Long teamId) {
//        Team team = teamQueryAdapter.findById(teamId);
//        return TeamMapper.toGetTeamWiki(team);
//    }
}