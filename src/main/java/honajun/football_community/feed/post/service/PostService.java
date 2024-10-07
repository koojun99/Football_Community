package honajun.football_community.feed.post.service;

import honajun.football_community.feed.category.Category;
import honajun.football_community.feed.category.CategoryQueryAdapter;
import honajun.football_community.feed.exception.FeedException;
import honajun.football_community.feed.exception.FeedExceptionCode;
import honajun.football_community.feed.post.dto.PostRequestDTO;
import honajun.football_community.feed.post.dto.PostResponseDTO;
import honajun.football_community.feed.post.entity.Post;
import honajun.football_community.feed.post.mapper.PostMapper;
import honajun.football_community.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostCommandAdapter postCommandAdapter;
    private final PostQueryAdapter postQueryAdapter;

    private final CategoryQueryAdapter categoryQueryAdapter;

    @Transactional
    public PostResponseDTO.getPostId createPost(Member member, PostRequestDTO.createPost request) {
        Category category = categoryQueryAdapter.findById(request.getCategoryId());
        Post savedPost = postCommandAdapter.createPost(PostMapper.toPost(member, category, request));
        return PostMapper.toGetPostId(savedPost);
    }

    @Transactional(readOnly = true)
    public PostResponseDTO.postDetail getPostDetail(Long postId) {
        Post post = postQueryAdapter.findById(postId);
        return PostMapper.toPostDetail(post);
    }

    @Transactional
    public PostResponseDTO.getPostId updatePost(Member member, Long postId, PostRequestDTO.updatePost request) {
        Post post = postQueryAdapter.findById(postId);
        if (!post.isWriter(member.getId())) {
            throw new FeedException(FeedExceptionCode._WRONG_WRITER);
        }
        postCommandAdapter.updatePost(post, request);
        return PostMapper.toGetPostId(post);
    }

    @Transactional
    public void deletePost(Member member, Long postId) {
        Post post = postQueryAdapter.findById(postId);
        if (!post.isWriter(member.getId())) {
            throw new FeedException(FeedExceptionCode._WRONG_WRITER);
        }
        postCommandAdapter.deletePost(post);
    }
}
