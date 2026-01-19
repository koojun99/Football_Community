package honajun.football_community.favorite.repository;

import honajun.football_community.favorite.entity.Favorite;
import honajun.football_community.global.enums.favorite.FavoriteType;
import honajun.football_community.member.entity.Member;
import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findAllByMember(Member member);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT f FROM Favorite f WHERE f.member = :member AND f.targetId = :targetId AND f.favoriteType = :favoriteType")
    Optional<Favorite> findByMemberAndTargetIdAndFavoriteTypeWithLock(Member member, Long targetId,
            FavoriteType favoriteType);

    // 특정 타입의 즐겨찾기 목록 조회
    List<Favorite> findAllByMemberAndFavoriteType(Member member, FavoriteType favoriteType);

    // 특정 타입의 모든 즐겨찾기 목록 조회 (스케줄러용)
    List<Favorite> findAllByFavoriteType(FavoriteType favoriteType);
}