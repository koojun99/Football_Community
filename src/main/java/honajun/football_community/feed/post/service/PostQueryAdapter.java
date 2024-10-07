package honajun.football_community.feed.post.service;

import honajun.football_community.feed.exception.FeedException;
import honajun.football_community.feed.exception.FeedExceptionCode;
import honajun.football_community.feed.post.entity.Post;
import honajun.football_community.feed.post.repository.PostRepository;
import honajun.football_community.global.annotation.Adapter;
import lombok.RequiredArgsConstructor;

@Adapter
@RequiredArgsConstructor
public class PostQueryAdapter {

    private final PostRepository postRepository;

    public Post findById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new FeedException(FeedExceptionCode._POST_NOT_FOUND));
    }
}
