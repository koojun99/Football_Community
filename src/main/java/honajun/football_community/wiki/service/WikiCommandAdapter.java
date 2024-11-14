package honajun.football_community.wiki.service;

import honajun.football_community.wiki.WikiCategoryRepository;
import honajun.football_community.wiki.WikiRequestDTO.createCategory;
import honajun.football_community.wiki.WikiRequestDTO.updateWiki;
import honajun.football_community.wiki.entity.WikiCategory;
import lombok.RequiredArgsConstructor;
import honajun.football_community.global.annotation.Adapter;
import honajun.football_community.wiki.repository.WikiRepository;

@Adapter
@RequiredArgsConstructor
public class WikiCommandAdapter {

    private final WikiRepository wikiRepository;
    private final WikiCategoryRepository wikiCategoryRepository;

    public void updateWiki(WikiCategory wikiCategory, updateWiki request) {
        wikiCategory.update(request.getContent());
        wikiCategoryRepository.save(wikiCategory);
    }

    public void createCategory(WikiCategory wikiCategory) {
        wikiCategoryRepository.save(wikiCategory);
    }
}