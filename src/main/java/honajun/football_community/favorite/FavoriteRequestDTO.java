package honajun.football_community.favorite;

import honajun.football_community.global.enums.favorite.FavoriteType;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FavoriteRequestDTO {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class addFavorite {
        private Long targetId;
        private FavoriteType favoriteType;
    }
}