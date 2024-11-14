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

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteCommandAdapter favoriteCommandAdapter;
    private final FavoriteQueryAdapter favoriteQueryAdapter;
    private final LeagueQueryAdapter leagueQueryAdapter;
    private final TeamQueryAdapter teamQueryAdapter;

    @Transactional
    public FavoriteResponseDTO.getFavorite addFavorite(Member member, FavoriteRequestDTO.addFavorite request) {
        // 대상 유효성 검증
        validateTarget(request.getTargetId(), request.getFavoriteType());
        Favorite favorite = FavoriteMapper.toFavorite(member, request);
        favoriteCommandAdapter.save(favorite);
        return FavoriteMapper.toGetFavorite(favorite);
    }

    private void validateTarget(Long targetId, FavoriteType favoriteType) {
        if (favoriteType==FavoriteType.FAVORITE_LEAGUE) {
            leagueQueryAdapter.findById(targetId);
        } else if (favoriteType==FavoriteType.FAVORITE_TEAM) {
            teamQueryAdapter.findById(targetId);
        } else {
            throw new FavoriteException(FavoriteExceptionCode.INVALID_FAVORITE_TARGET);
        }
    }

    @Transactional
    public void deleteFavorite(Long favoriteId) {
        Favorite favorite = favoriteQueryAdapter.findById(favoriteId);

        favoriteCommandAdapter.delete(favorite);
    }

    public void toggleNotification(Member member, Long favoriteId) {
        Favorite favorite = favoriteQueryAdapter.findById(favoriteId);
        favorite.toggleNotification();
        favoriteCommandAdapter.save(favorite);
    }

    @Transactional(readOnly = true)
    public FavoriteResponseDTO.getFavorites getFavorites(Member member) {
        List<Favorite> favorites = favoriteQueryAdapter.findAllByMember(member);
        return FavoriteMapper.toGetFavorites(favorites);
    }
}