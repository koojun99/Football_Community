package honajun.football_community.favorite.repository;

import honajun.football_community.favorite.entity.Favorite;
import honajun.football_community.global.enums.favorite.FavoriteType;
import honajun.football_community.member.entity.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findAllByMember(Member member);

    boolean existsByMemberAndTargetIdAndFavoriteType(Member member, Long targetId, FavoriteType favoriteType);
}