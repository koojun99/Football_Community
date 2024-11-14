package honajun.football_community.favorite.mapper;

import honajun.football_community.favorite.dto.FavoriteRequestDTO;
import honajun.football_community.favorite.dto.FavoriteResponseDTO;
import honajun.football_community.favorite.dto.FavoriteResponseDTO.getFavorites;
import honajun.football_community.favorite.entity.Favorite;
import honajun.football_community.member.entity.Member;
import java.util.List;

public class FavoriteMapper {

    public static Favorite toFavorite(Member member, FavoriteRequestDTO.addFavorite request) {
        return Favorite.builder()
                .targetId(request.getTargetId())
                .favoriteType(request.getFavoriteType())
                .member(member)
                .isPushed(false)
                .build();
    }

    public static FavoriteResponseDTO.getFavorite toGetFavorite(Favorite favorite) {
        return FavoriteResponseDTO.getFavorite.builder()
                .id(favorite.getId())
                .favoriteType(favorite.getFavoriteType())
                .isPushed(favorite.isPushed())
                .build();
    }

    public static getFavorites toGetFavorites(List<Favorite> favorites) {
        return getFavorites.builder()
                .favorites(favorites.stream().map(FavoriteMapper::toGetFavorite).toList())
                .build();
    }
}
