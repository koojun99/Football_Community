package honajun.football_community.wiki.service;

import honajun.football_community.wiki.WikiRequestDTO;
import honajun.football_community.wiki.dto.WikiResponseDTO;
import honajun.football_community.wiki.entity.Wiki;
import honajun.football_community.wiki.entity.WikiCategory;
import honajun.football_community.wiki.mapper.WikiMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WikiService {

    private final WikiCommandAdapter wikiCommandAdapter;
    private final WikiQueryAdapter wikiQueryAdapter;

    @Cacheable(cacheNames = "wikis", key = "#wikiId")
    @Transactional(readOnly = true)
    public WikiResponseDTO.getWiki getWiki(Long wikiId) {
        Wiki wiki = wikiQueryAdapter.findById(wikiId);
        List<WikiCategory> categories = wikiQueryAdapter.findWikiCategoriesByWikiId(wikiId);
        return WikiMapper.toGetWiki(wiki, categories);
    }

    @Cacheable(cacheNames = "wikiList")
    @Transactional(readOnly = true)
    public WikiResponseDTO.getWikiList getWikiList() {
        List<Wiki> wikis = wikiQueryAdapter.findAll();
        return WikiMapper.toGetWikiList(wikis, wikiQueryAdapter);
    }

    @CacheEvict(cacheNames = "wikiList", allEntries = true)
    @Transactional
    public WikiResponseDTO.getWikiId createWiki(WikiRequestDTO.createWiki request) {
        Wiki wiki = WikiMapper.toWiki(request);
        Wiki savedWiki = wikiCommandAdapter.createWiki(wiki);
        return WikiResponseDTO.getWikiId.builder()
                .id(savedWiki.getId())
                .build();
    }

    @CacheEvict(cacheNames = "wikis", key = "#wikiId")
    @Transactional
    public void createCategory(Long wikiId, WikiRequestDTO.createCategory request) {
        Wiki wiki = wikiQueryAdapter.findById(wikiId);
        // 새로운 카테고리는 마지막 순서로 추가
        Integer maxOrderIndex = wikiQueryAdapter.findMaxOrderIndexByWikiId(wikiId);
        Integer newOrderIndex = maxOrderIndex + 1;
        WikiCategory wikiCategory = WikiMapper.toWikiCategory(wiki, request, newOrderIndex);
        wikiCommandAdapter.createCategory(wikiCategory);
    }

    @CacheEvict(cacheNames = "wikis", allEntries = true)
    @Transactional
    public void updateWikiCategory(Long wikiId, Long wikiCategoryId, WikiRequestDTO.updateWiki request) {
        WikiCategory wikiCategory = wikiQueryAdapter.findWikiCategoryById(wikiCategoryId);
        // 같은 위키의 카테고리인지 확인
        if (!wikiCategory.getWiki().getId().equals(wikiId)) {
            throw new honajun.football_community.wiki.exception.WikiException(
                    honajun.football_community.wiki.exception.WikiExceptionCode.WIKI_CATEGORY_NOT_FOUND);
        }
        wikiCommandAdapter.updateWiki(wikiCategory, request);
    }
    
    @CacheEvict(cacheNames = "wikis", key = "#wikiId")
    @Transactional
    public void deleteCategory(Long wikiId, Long wikiCategoryId) {
        WikiCategory wikiCategory = wikiQueryAdapter.findWikiCategoryById(wikiCategoryId);
        // 같은 위키의 카테고리인지 확인
        if (!wikiCategory.getWiki().getId().equals(wikiId)) {
            throw new honajun.football_community.wiki.exception.WikiException(
                    honajun.football_community.wiki.exception.WikiExceptionCode.WIKI_CATEGORY_NOT_FOUND);
        }
        wikiCommandAdapter.deleteCategory(wikiCategory);
    }
}