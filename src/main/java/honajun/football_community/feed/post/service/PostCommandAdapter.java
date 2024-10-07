package honajun.football_community.feed.post.service;

import honajun.football_community.feed.post.dto.PostRequestDTO;
import honajun.football_community.feed.post.entity.Post;
import honajun.football_community.feed.post.repository.PostRepository;
import honajun.football_community.global.annotation.Adapter;
import lombok.RequiredArgsConstructor;

@Adapter
@RequiredArgsConstructor
public class PostCommandAdapter {

    private final PostRepository postRepository;

    public Post createPost(Post post) {
        return postRepository.save(post);
    }

    public void updatePost(Post post, PostRequestDTO.updatePost request) {
        post.update(request.getTitle(), request.getBody());
    }

    public void deletePost(Post post) {
        postRepository.delete(post);
    }
}
