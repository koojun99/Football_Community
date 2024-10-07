package honajun.football_community.feed.post.controller;

import honajun.football_community.feed.post.dto.PostRequestDTO;
import honajun.football_community.feed.post.dto.PostResponseDTO;
import honajun.football_community.feed.post.service.PostService;
import honajun.football_community.global.annotation.AuthMember;
import honajun.football_community.global.response.CommonResponse;
import honajun.football_community.member.entity.Member;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/feed/posts")
public class PostController {

    private final PostService postService;

    @Operation(summary = "게시글 작성", description = "게시글을 작성합니다.")
    @PostMapping
    public CommonResponse<PostResponseDTO.getPostId> createPost(
            @AuthMember @Parameter(hidden = true) Member member,
            @RequestBody PostRequestDTO.createPost request
    ) {
        return CommonResponse.onSuccess(postService.createPost(member, request));
    }

    @Operation(summary = "게시글 상세 조회", description = "게시글의 상세 정보를 조회합니다.")
    @GetMapping("/{postId}")
    public CommonResponse<PostResponseDTO.postDetail> getPostDetail(
            @PathVariable Long postId
    ) {
        return CommonResponse.onSuccess(postService.getPostDetail(postId));
    }

    @Operation(summary = "게시글 수정", description = "게시글을 수정합니다.")
    @PutMapping("/{postId}")
    public CommonResponse<PostResponseDTO.getPostId> updatePost(
            @AuthMember @Parameter(hidden = true) Member member,
            @PathVariable Long postId,
            @RequestBody PostRequestDTO.updatePost request
    ) {
        return CommonResponse.onSuccess(postService.updatePost(member, postId, request));
    }

    @Operation(summary = "게시글 삭제", description = "게시글을 삭제합니다.")
    @DeleteMapping("/{postId}")
    public CommonResponse<Void> deletePost(
            @AuthMember @Parameter(hidden = true) Member member,
            @PathVariable Long postId
    ) {
        postService.deletePost(member, postId);
        return CommonResponse.onNoContent();
    }
}
