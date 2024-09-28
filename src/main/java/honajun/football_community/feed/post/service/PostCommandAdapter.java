package honajun.football_community.feed.post.service;

import honajun.football_community.feed.post.repository.PostRepository;
import honajun.football_community.global.annotation.Adapter;
import lombok.RequiredArgsConstructor;

@Adapter
@RequiredArgsConstructor
public class PostCommandAdapter {

    private final PostRepository postRepository;
}
