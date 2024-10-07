package honajun.football_community.feed.post.mapper;

import honajun.football_community.feed.category.Category;
import honajun.football_community.feed.post.dto.PostRequestDTO;
import honajun.football_community.feed.post.dto.PostResponseDTO;
import honajun.football_community.feed.post.entity.Post;
import honajun.football_community.member.entity.Member;

public class PostMapper {

    public static Post toPost(Member member, Category category, PostRequestDTO.createPost createPost) {
        return Post.builder()
                .category(category)
                .title(createPost.getTitle())
                .body(createPost.getBody())
                .writer(member)
                .build();
    }

    public static PostResponseDTO.getPostId toGetPostId(Post post) {
        return PostResponseDTO.getPostId.builder()
                .postId(post.getId())
                .build();
    }

    public static PostResponseDTO.postDetail toPostDetail(Post post) {
        return PostResponseDTO.postDetail.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .body(post.getBody())
                .writerId(post.getWriter().getId())
                .writerName(post.getWriter().getUsername())
                .build();
    }
}
