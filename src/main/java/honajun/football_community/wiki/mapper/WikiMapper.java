package honajun.football_community.wiki.mapper;

import honajun.football_community.wiki.WikiRequestDTO.createCategory;
import honajun.football_community.wiki.dto.WikiResponseDTO;
import honajun.football_community.wiki.dto.WikiResponseDTO.getCategory;
import honajun.football_community.wiki.entity.Wiki;
import honajun.football_community.wiki.entity.WikiCategory;
import java.util.List;
import java.util.stream.Collectors;

public class WikiMapper {

    public static WikiResponseDTO.getWiki toGetWiki(Wiki wiki, List<WikiCategory> categories) {
        return WikiResponseDTO.getWiki.builder()
                .id(wiki.getId())
                .title(wiki.getTitle())
                .categories(toGetCategories(categories))
                .build();
    }

    public static WikiCategory toWikiCategory(Wiki wiki, createCategory request) {
        return WikiCategory.builder()
                .wiki(wiki)
                .name(request.getName())
                .description(request.getDescription())
                .build();
    }

    private static List<getCategory> toGetCategories(List<WikiCategory> categories) {
        return categories.stream()
                .map(category -> WikiResponseDTO.getCategory.builder()
                        .id(category.getId())
                        .name(category.getName())
                        .description(category.getDescription())
                        .build())
                .collect(Collectors.toList());
    }
}
