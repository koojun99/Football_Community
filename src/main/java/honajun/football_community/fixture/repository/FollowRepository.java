package honajun.football_community.fixture.repository;

import honajun.football_community.fixture.entity.Follow;
import honajun.football_community.member.entity.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {

    List<Follow> findAllByMember(Member member);

    Optional<Follow> findByMemberAndFixtureId(Member member, Long fixtureId);

    boolean existsByMemberAndFixtureId(Member member, Long fixtureId);

    boolean existsByFixtureId(Long fixtureId);

    // 스케줄러용: Follow된 모든 fixtureId 목록 조회 (중복 제거)
    @Query("SELECT DISTINCT f.fixtureId FROM Follow f")
    List<Long> findAllDistinctFixtureIds();

    // 특정 fixtureId를 Follow한 모든 Follow 객체 조회
    List<Follow> findAllByFixtureId(Long fixtureId);
}
