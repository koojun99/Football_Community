package honajun.football_community.favorite.dto;

import honajun.football_community.global.enums.favorite.FavoriteType;
import java.util.List;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FavoriteResponseDTO {

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class getFavorite {
        private Long id;
        private FavoriteType favoriteType;
        private Long targetId;
    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class getFavorites {
        List<getFavorite> favorites;
    }
}