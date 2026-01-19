package honajun.football_community.fixture.mapper;

import honajun.football_community.fixture.dto.FollowRequestDTO;
import honajun.football_community.fixture.dto.FollowResponseDTO;
import honajun.football_community.fixture.dto.FollowResponseDTO.getFollows;
import honajun.football_community.fixture.entity.Follow;
import honajun.football_community.member.entity.Member;
import java.util.List;

public class FollowMapper {

    public static Follow toFollow(Member member, FollowRequestDTO.addFollow request) {
        return Follow.builder()
                .fixtureId(request.getFixtureId())
                .member(member)
                .build();
    }

    public static FollowResponseDTO.getFollow toGetFollow(Follow follow) {
        return FollowResponseDTO.getFollow.builder()
                .id(follow.getId())
                .fixtureId(follow.getFixtureId())
                .build();
    }

    public static getFollows toGetFollows(List<Follow> follows) {
        return getFollows.builder()
                .follows(follows.stream().map(FollowMapper::toGetFollow).toList())
                .build();
    }
}

