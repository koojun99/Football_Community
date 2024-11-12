package honajun.football_community.league.mapper;

import honajun.football_community.league.dto.LeagueResponseDTO;
import honajun.football_community.league.entity.League;

public class LeagueMapper {

    public static LeagueResponseDTO.getLeagueWiki toGetLeagueWiki(League league) {
        return LeagueResponseDTO.getLeagueWiki.builder()
                .id(league.getId())
                .division(league.getDivision())
                .name(league.getName())
                .country(league.getCountry())
                .founded(league.getFounded())
                .mostRecentChampion(league.getMostRecentChampion())
                .mostWinningTeam(league.getMostWinningTeam())
                .build();
    }
}
