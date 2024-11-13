package honajun.football_community.wiki.service;

import honajun.football_community.wiki.entity.Wiki;
import honajun.football_community.wiki.exception.WikiException;
import honajun.football_community.wiki.exception.WikiExceptionCode;
import lombok.RequiredArgsConstructor;
import honajun.football_community.global.annotation.Adapter;
import honajun.football_community.wiki.repository.WikiRepository;

@Adapter
@RequiredArgsConstructor
public class WikiQueryAdapter {

    private final WikiRepository wikiRepository;

    public Wiki findById(Long wikiId) {
        return wikiRepository.findById(wikiId).orElseThrow(() -> new WikiException(
                WikiExceptionCode._WIKI_NOT_FOUND));
    }
}