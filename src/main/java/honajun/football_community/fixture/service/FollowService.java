package honajun.football_community.fixture.service;

import honajun.football_community.fixture.dto.FollowRequestDTO;
import honajun.football_community.fixture.dto.FollowResponseDTO;
import honajun.football_community.fixture.entity.Follow;
import honajun.football_community.fixture.exception.FixtureException;
import honajun.football_community.fixture.exception.FixtureExceptionCode;
import honajun.football_community.fixture.mapper.FollowMapper;
import honajun.football_community.member.entity.Member;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Follow 관련 비즈니스 로직을 처리하는 서비스
 */
@Service
@RequiredArgsConstructor
public class FollowService {

    private final FollowCommandAdapter followCommandAdapter;
    private final FollowQueryAdapter followQueryAdapter;

    /**
     * 경기를 Follow합니다.
     */
    @Transactional
    public FollowResponseDTO.getFollow addFollow(Member member, FollowRequestDTO.addFollow request) {
        validateDuplicateFollow(member, request.getFixtureId());
        Follow follow = FollowMapper.toFollow(member, request);
        followCommandAdapter.save(follow);
        return FollowMapper.toGetFollow(follow);
    }

    private void validateDuplicateFollow(Member member, Long fixtureId) {
        boolean exists = followQueryAdapter.existsByMemberAndFixtureId(member, fixtureId);
        if (exists) {
            throw new FixtureException(FixtureExceptionCode.DUPLICATE_FOLLOW);
        }
    }

    /**
     * Follow를 삭제합니다.
     */
    @Transactional
    public void deleteFollow(Long followId) {
        Follow follow = followQueryAdapter.findById(followId);
        followCommandAdapter.delete(follow);
    }

    /**
     * 사용자의 Follow 목록을 조회합니다.
     */
    @Transactional(readOnly = true)
    public FollowResponseDTO.getFollows getFollows(Member member) {
        List<Follow> follows = followQueryAdapter.findAllByMember(member);
        return FollowMapper.toGetFollows(follows);
    }
}

