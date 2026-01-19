package honajun.football_community.favorite.service;

import honajun.football_community.favorite.entity.Favorite;
import honajun.football_community.favorite.exception.FavoriteException;
import honajun.football_community.favorite.exception.FavoriteExceptionCode;
import honajun.football_community.global.enums.favorite.FavoriteType;
import honajun.football_community.member.entity.Member;
import java.util.List;
import java.util.Optional;
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

    public Optional<Favorite> existsByMemberAndTargetIdAndFavoriteType(Member member, Long targetId,
            FavoriteType favoriteType) {
        return favoriteRepository.findByMemberAndTargetIdAndFavoriteTypeWithLock(member, targetId, favoriteType);
    }

    // 특정 타입의 즐겨찾기 목록 조회
    public List<Favorite> findAllByMemberAndFavoriteType(Member member, FavoriteType favoriteType) {
        return favoriteRepository.findAllByMemberAndFavoriteType(member, favoriteType);
    }

    // 특정 타입의 모든 즐겨찾기 목록 조회 (스케줄러용)
    public List<Favorite> findAllByFavoriteType(FavoriteType favoriteType) {
        return favoriteRepository.findAllByFavoriteType(favoriteType);
    }
}