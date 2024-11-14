package honajun.football_community.favorite.mapper;

import honajun.football_community.favorite.FavoriteRequestDTO;
import honajun.football_community.favorite.FavoriteResponseDTO;
import honajun.football_community.favorite.entity.Favorite;
import honajun.football_community.member.entity.Member;

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
}
