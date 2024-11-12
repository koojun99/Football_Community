package honajun.football_community.team.service;

import lombok.RequiredArgsConstructor;
import honajun.football_community.global.annotation.Adapter;
import honajun.football_community.team.repository.TeamRepository;

@Adapter
@RequiredArgsConstructor
public class TeamCommandAdapter {

    private final TeamRepository TeamRepository;
}