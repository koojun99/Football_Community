package honajun.football_community.favorite;

import honajun.football_community.global.enums.favorite.FavoriteType;
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
        private boolean isPushed;
    }

}