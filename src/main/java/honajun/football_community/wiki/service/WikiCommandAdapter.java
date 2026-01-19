package honajun.football_community.wiki.service;

import honajun.football_community.wiki.WikiCategoryRepository;
import honajun.football_community.wiki.WikiRequestDTO.updateWiki;
import honajun.football_community.wiki.entity.Wiki;
import honajun.football_community.wiki.entity.WikiCategory;
import honajun.football_community.wiki.repository.WikiRepository;
import lombok.RequiredArgsConstructor;
import honajun.football_community.global.annotation.Adapter;

@Adapter
@RequiredArgsConstructor
public class WikiCommandAdapter {

    private final WikiRepository wikiRepository;
    private final WikiCategoryRepository wikiCategoryRepository;

    public Wiki createWiki(Wiki wiki) {
        return wikiRepository.save(wiki);
    }

    public void updateWiki(WikiCategory wikiCategory, updateWiki request) {
        wikiCategory.update(request.getName(), request.getDescription());
        wikiCategoryRepository.save(wikiCategory);
    }

    public void createCategory(WikiCategory wikiCategory) {
        wikiCategoryRepository.save(wikiCategory);
    }
    
    public void deleteCategory(WikiCategory wikiCategory) {
        wikiCategoryRepository.delete(wikiCategory);
    }
}