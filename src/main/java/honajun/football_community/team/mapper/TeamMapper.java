package honajun.football_community.team.mapper;

import honajun.football_community.team.dto.TeamResponseDTO;
import honajun.football_community.team.entity.Team;

public class TeamMapper {

    public static TeamResponseDTO.getTeamWiki toGetTeamWiki(Team team) {
        return TeamResponseDTO.getTeamWiki.builder()
                .id(team.getId())
                .name(team.getName())
                .founded(team.getFounded())
                .stadium(team.getStadium())
                .headCoach(team.getHeadCoach())
                .leagueId(team.getLeague().getId())
                .build();
    }
}
