package honajun.football_community.wiki.service;

import lombok.RequiredArgsConstructor;
import honajun.football_community.global.annotation.Adapter;
import honajun.football_community.wiki.repository.WikiRepository;

@Adapter
@RequiredArgsConstructor
public class WikiCommandAdapter {

    private final WikiRepository wikiRepository;
}