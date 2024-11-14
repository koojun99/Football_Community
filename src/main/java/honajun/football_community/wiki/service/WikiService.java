package honajun.football_community.wiki.service;

import honajun.football_community.wiki.WikiRequestDTO;
import honajun.football_community.wiki.dto.WikiResponseDTO;
import honajun.football_community.wiki.entity.Wiki;
import honajun.football_community.wiki.entity.WikiCategory;
import honajun.football_community.wiki.mapper.WikiMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import honajun.football_community.wiki.service.WikiCommandAdapter;
import honajun.football_community.wiki.service.WikiQueryAdapter;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WikiService {

    private final WikiCommandAdapter wikiCommandAdapter;
    private final WikiQueryAdapter wikiQueryAdapter;

    @Transactional(readOnly = true)
    public WikiResponseDTO.getWiki getWiki(Long wikiId) {
        Wiki wiki = wikiQueryAdapter.findById(wikiId);
        return WikiMapper.toGetWiki(wiki);
    }

    @Transactional
    public void createCategory(Long wikiId, WikiRequestDTO.createCategory request) {
        Wiki wiki = wikiQueryAdapter.findById(wikiId);
        WikiCategory wikiCategory = WikiMapper.toWikiCategory(wiki, request);
        wikiCommandAdapter.createCategory(wikiCategory);
    }

    @Transactional
    public void updateWiki(Long wikiCategoryId, WikiRequestDTO.updateWiki request) {
        WikiCategory wikiCategory = wikiQueryAdapter.findWikiCategoryById(wikiCategoryId);
        wikiCommandAdapter.updateWiki(wikiCategory, request);
    }
}