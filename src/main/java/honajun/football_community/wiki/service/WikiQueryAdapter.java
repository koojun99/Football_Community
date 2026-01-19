package honajun.football_community.wiki.service;

import honajun.football_community.wiki.WikiCategoryRepository;
import honajun.football_community.wiki.entity.Wiki;
import honajun.football_community.wiki.entity.WikiCategory;
import honajun.football_community.wiki.exception.WikiException;
import honajun.football_community.wiki.exception.WikiExceptionCode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import honajun.football_community.global.annotation.Adapter;
import honajun.football_community.wiki.repository.WikiRepository;

@Adapter
@RequiredArgsConstructor
public class WikiQueryAdapter {

    private final WikiRepository wikiRepository;
    private final WikiCategoryRepository wikiCategoryRepository;

    public Wiki findById(Long wikiId) {
        return wikiRepository.findById(wikiId).orElseThrow(() -> new WikiException(
                WikiExceptionCode.WIKI_NOT_FOUND));
    }

    public WikiCategory findWikiCategoryById(Long wikiCategoryId) {
        return wikiCategoryRepository.findById(wikiCategoryId).orElseThrow(() -> new WikiException(
                WikiExceptionCode.WIKI_CATEGORY_NOT_FOUND));
    }

    public List<WikiCategory> findWikiCategoriesByWikiId(Long wikiId) {
        return wikiCategoryRepository.findByWiki_IdOrderByOrderIndexAsc(wikiId);
    }
    
    public Integer findMaxOrderIndexByWikiId(Long wikiId) {
        Integer maxOrderIndex = wikiCategoryRepository.findMaxOrderIndexByWiki_Id(wikiId);
        return maxOrderIndex != null ? maxOrderIndex : 0;
    }

    public List<Wiki> findAll() {
        return wikiRepository.findAll();
    }
}