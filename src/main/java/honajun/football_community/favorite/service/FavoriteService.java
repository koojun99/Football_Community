package honajun.football_community.favorite.service;

import honajun.football_community.favorite.dto.FavoriteRequestDTO;
import honajun.football_community.favorite.dto.FavoriteResponseDTO;
import honajun.football_community.favorite.entity.Favorite;
import honajun.football_community.favorite.exception.FavoriteException;
import honajun.football_community.favorite.exception.FavoriteExceptionCode;
import honajun.football_community.favorite.mapper.FavoriteMapper;
import honajun.football_community.global.enums.favorite.FavoriteType;
import honajun.football_community.league.service.LeagueQueryAdapter;
import honajun.football_community.member.entity.Member;
import honajun.football_community.team.service.TeamQueryAdapter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 즐겨찾기 관련 비즈니스 로직을 처리하는 서비스
 */
@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteCommandAdapter favoriteCommandAdapter;
    private final FavoriteQueryAdapter favoriteQueryAdapter;
    private final LeagueQueryAdapter leagueQueryAdapter;
    private final TeamQueryAdapter teamQueryAdapter;

    /**
     * 즐겨찾기를 추가합니다.
     */
    @Transactional
    public FavoriteResponseDTO.getFavorite addFavorite(Member member, FavoriteRequestDTO.addFavorite request) {
        validateDuplicateFavorite(member, request.getTargetId(), request.getFavoriteType());
        // 대상 유효성 검증
        validateTarget(request.getTargetId(), request.getFavoriteType());
        Favorite favorite = FavoriteMapper.toFavorite(member, request);
        favoriteCommandAdapter.save(favorite);
        return FavoriteMapper.toGetFavorite(favorite);
    }

    private void validateDuplicateFavorite(Member member, Long targetId, FavoriteType favoriteType) {
        boolean exists = favoriteQueryAdapter.existsByMemberAndTargetIdAndFavoriteType(member, targetId, favoriteType)
                .isPresent();
        if (exists) {
            throw new FavoriteException(FavoriteExceptionCode.DUPLICATE_FAVORITE);
        }
    }

    /**
     * 즐겨찾기 대상의 유효성을 검증합니다.
     * FAVORITE_MATCH의 경우 외부 API의 fixture ID이므로 별도의 검증을 하지 않습니다.
     */
    private void validateTarget(Long targetId, FavoriteType favoriteType) {
        if (favoriteType == FavoriteType.FAVORITE_LEAGUE) {
            leagueQueryAdapter.findById(targetId);
        } else if (favoriteType == FavoriteType.FAVORITE_TEAM) {
            teamQueryAdapter.findById(targetId);
        } else if (favoriteType == FavoriteType.FAVORITE_MATCH) {
            // FAVORITE_MATCH는 외부 API의 fixture ID이므로 별도 검증 없음
            // 실제 경기 정보는 외부 API에서 조회
        } else {
            throw new FavoriteException(FavoriteExceptionCode.INVALID_FAVORITE_TARGET);
        }
    }

    /**
     * 즐겨찾기를 삭제합니다.
     */
    @Transactional
    public void deleteFavorite(Long favoriteId) {
        Favorite favorite = favoriteQueryAdapter.findById(favoriteId);

        favoriteCommandAdapter.delete(favorite);
    }

    /**
     * 사용자의 즐겨찾기 목록을 조회합니다.
     */
    @Transactional(readOnly = true)
    public FavoriteResponseDTO.getFavorites getFavorites(Member member) {
        List<Favorite> favorites = favoriteQueryAdapter.findAllByMember(member);
        return FavoriteMapper.toGetFavorites(favorites);
    }
}