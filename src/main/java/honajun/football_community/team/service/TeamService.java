package honajun.football_community.team.service;

import honajun.football_community.team.dto.TeamResponseDTO;
import honajun.football_community.team.entity.Team;
import honajun.football_community.team.mapper.TeamMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamCommandAdapter teamCommandAdapter;
    private final TeamQueryAdapter teamQueryAdapter;
}