package honajun.football_community.favorite.service;

import honajun.football_community.favorite.entity.Favorite;
import honajun.football_community.favorite.exception.FavoriteException;
import honajun.football_community.favorite.exception.FavoriteExceptionCode;
import honajun.football_community.global.enums.favorite.FavoriteType;
import honajun.football_community.member.entity.Member;
import java.util.List;
import lombok.RequiredArgsConstructor;
import honajun.football_community.global.annotation.Adapter;
import honajun.football_community.favorite.repository.FavoriteRepository;

@Adapter
@RequiredArgsConstructor
public class FavoriteQueryAdapter {

    private final FavoriteRepository favoriteRepository;

    public Favorite findById(Long favoriteId) {
        return favoriteRepository.findById(favoriteId)
                .orElseThrow(() -> new FavoriteException(FavoriteExceptionCode.FAVORITE_NOT_FOUND));
    }

    public List<Favorite> findAllByMember(Member member) {
        return favoriteRepository.findAllByMember(member);
    }

    public boolean existsByMemberAndTargetIdAndFavoriteType(Member member, Long targetId, FavoriteType favoriteType) {
        return favoriteRepository.existsByMemberAndTargetIdAndFavoriteType(member, targetId, favoriteType);
    }
}