package honajun.football_community.fixture.service;

import honajun.football_community.fixture.entity.Follow;
import honajun.football_community.fixture.repository.FollowRepository;
import honajun.football_community.global.annotation.Adapter;
import lombok.RequiredArgsConstructor;

@Adapter
@RequiredArgsConstructor
public class FollowCommandAdapter {

    private final FollowRepository followRepository;

    public void save(Follow follow) {
        followRepository.save(follow);
    }

    public void delete(Follow follow) {
        followRepository.delete(follow);
    }
}
