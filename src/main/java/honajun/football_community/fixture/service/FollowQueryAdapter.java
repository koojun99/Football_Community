package honajun.football_community.fixture.service;

import honajun.football_community.fixture.entity.Follow;
import honajun.football_community.fixture.exception.FixtureException;
import honajun.football_community.fixture.exception.FixtureExceptionCode;
import honajun.football_community.fixture.repository.FollowRepository;
import honajun.football_community.global.annotation.Adapter;
import honajun.football_community.member.entity.Member;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@Adapter
@RequiredArgsConstructor
public class FollowQueryAdapter {

    private final FollowRepository followRepository;

    public Follow findById(Long followId) {
        return followRepository.findById(followId)
                .orElseThrow(() -> new FixtureException(FixtureExceptionCode.FOLLOW_NOT_FOUND));
    }

    public List<Follow> findAllByMember(Member member) {
        return followRepository.findAllByMember(member);
    }

    public Optional<Follow> findByMemberAndFixtureId(Member member, Long fixtureId) {
        return followRepository.findByMemberAndFixtureId(member, fixtureId);
    }

    public boolean existsByMemberAndFixtureId(Member member, Long fixtureId) {
        return followRepository.existsByMemberAndFixtureId(member, fixtureId);
    }

    // 스케줄러용: Follow된 모든 fixtureId 목록 조회 (중복 제거)
    public List<Long> findAllDistinctFixtureIds() {
        return followRepository.findAllDistinctFixtureIds();
    }

    // 특정 fixtureId를 Follow한 모든 Follow 객체 조회
    public List<Follow> findAllByFixtureId(Long fixtureId) {
        return followRepository.findAllByFixtureId(fixtureId);
    }
}

