package honajun.football_community.feed.category;

import honajun.football_community.feed.exception.FeedException;
import honajun.football_community.feed.exception.FeedExceptionCode;
import honajun.football_community.global.annotation.Adapter;
import lombok.RequiredArgsConstructor;

@Adapter
@RequiredArgsConstructor
public class CategoryQueryAdapter {

    private final CategoryRepository categoryRepository;

    public Category findById(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new FeedException(FeedExceptionCode._CATEGORY_NOT_FOUND));
    }
}
