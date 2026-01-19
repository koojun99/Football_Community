package honajun.football_community.wiki.mapper;

import honajun.football_community.wiki.WikiRequestDTO;
import honajun.football_community.wiki.WikiRequestDTO.createCategory;
import honajun.football_community.wiki.dto.WikiResponseDTO;
import honajun.football_community.wiki.dto.WikiResponseDTO.getCategory;
import honajun.football_community.wiki.entity.Wiki;
import honajun.football_community.wiki.entity.WikiCategory;
import honajun.football_community.wiki.service.WikiQueryAdapter;
import java.util.List;
import java.util.stream.Collectors;

public class WikiMapper {

    public static Wiki toWiki(WikiRequestDTO.createWiki request) {
        return Wiki.builder()
                .title(request.getTitle())
                .teamId(request.getTeamId())
                .leagueId(request.getLeagueId())
                .build();
    }

    public static WikiResponseDTO.getWiki toGetWiki(Wiki wiki, List<WikiCategory> categories) {
        return WikiResponseDTO.getWiki.builder()
                .id(wiki.getId())
                .title(wiki.getTitle())
                .categories(toGetCategories(categories))
                .build();
    }

    public static WikiCategory toWikiCategory(Wiki wiki, createCategory request, Integer orderIndex) {
        return WikiCategory.builder()
                .wiki(wiki)
                .name(request.getName())
                .description(request.getDescription())
                .orderIndex(orderIndex)
                .build();
    }

    private static List<getCategory> toGetCategories(List<WikiCategory> categories) {
        return categories.stream()
                .map(category -> WikiResponseDTO.getCategory.builder()
                        .id(category.getId())
                        .name(category.getName())
                        .description(category.getDescription())
                        .orderIndex(category.getOrderIndex())
                        .build())
                .collect(Collectors.toList());
    }

    public static WikiResponseDTO.getWikiList toGetWikiList(List<Wiki> wikis, WikiQueryAdapter wikiQueryAdapter) {
        List<WikiResponseDTO.getWiki> wikiList = wikis.stream()
                .map(wiki -> {
                    List<WikiCategory> categories = wikiQueryAdapter.findWikiCategoriesByWikiId(wiki.getId());
                    return toGetWiki(wiki, categories);
                })
                .collect(Collectors.toList());
        
        return WikiResponseDTO.getWikiList.builder()
                .wikis(wikiList)
                .build();
    }
}
